pipeline{
	agent any
	enviroment{
		APP_DIR="~/app"
		JAR_NAME="SpringRecipeAiProject-0.0.1-SNAPSHOT.jar"
	}
	stages{
		/*
			git push = commit (main)
				|
			web hook 
				|
			jenkins (local) ==> EC2
				|
			build
				|
			docker build
			docker push
				|
			docker pull
			docker run
			
			-name = stage
			 run: 명령어 => steps
		*/
		
		/*
			Repository: 소스파일 => git URL
		*/
		stage('Check Out'){
			steps{
				echo "Git Checkout"
				checkout scm
			}
		}
		
		//임시 파일
		stage('Create .env'){
			steps {
				withCredentiala([
					string(
						credentialsId: 'post-url',
						variable: 'POST_URL'
					),
					string(
						credentialsId: 'gen-key',
						variable: 'GEN_KEY'
					)
				]){
					sh '''
						cat > .env << EOF
						SPRING_PROFILES_ACTIVE=prod
						POST_URL=${POST_URL}
						GEN_KEY=${GEN_KEY}
						EOF
						  chmod 600 .env
					   '''
				}
			}
		}
		
		// gradlew build => permisson 처리
		stage('Gradlew Permission'){
			steps{
				sh '''
					 chmod +x gradlew
				   '''
			}
		}
		
		//gradlew build 
		stage('Gradlew Build'){
			steps{
				sh '''
					 ./gradlew clean build -x test
				   '''
			}
		}
		
		//Docker Build
		stage('Docker Build'){
			steps{
				sh '''
					 docker build -t cksdn858/ai-app:latest .
				   '''
			}
		}
		
		//DockerHub Login
		stage('DockerHub Login'){
			steps{
				withCredentiala([usernamePassword(
					credentialsId: 'dockerhub_info',
					usernameVariable: 'DH_USER',
					passwordVariable: 'DH_PASS',
				)]){
					sh '''
						echo "$DH_PASS" | docker login -u "$DH_USER" --password-stdin
					   '''
				}
			}
		}
		
		//Docker Push
		stage('Docker Push'){
			steps{
				sh '''
					 docker push cksdn858/ai-app:latest
				   '''	
			}
		}
		
		stage('Container Stop'){
			steps{
				sh '''
					docker stop ai-app || true
				   '''
			}
		}
		
		stage('Container Remove'){
			steps{
				sh '''
					docker rm ai-app || true
				   '''
			}
		}
		
		stage('DockerHub Pull'){
			steps{
				sh '''
					docker pull cksdn858/ai-app:latest
				   '''
			}
		}
		
		stage('Docker run'){
			steps{
				sh '''
					docker run -d --name ai-app -p 9090:9090 --env-file .env cksdn858/ai-app:latest
				   '''
			}
		}
	}
}