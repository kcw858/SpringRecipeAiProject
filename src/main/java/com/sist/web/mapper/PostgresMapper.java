package com.sist.web.mapper;
import java.util.*;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PostgresMapper {
	/*
	<select id="findSimilarRecipes" resultType="hashmap"> 
	  	SELECT id, recipe_id, content, 
	   	(embedding &lt;=> CAST(#{embedding} AS vector)) AS distance, 
	   	( 1 - (embedding &lt;=> CAST(#{embedding} AS vector)) ) AS similarity 
	  	FROM recipe_vector  ORDER BY embedding 
	  	&lt;=> CAST(#{embedding} AS vector) 
  		LIMIT #{limit} 
 	</select>
	 */
	public List<Map<String, Object>> findSimilarRecipes( 
			@Param("embedding") String embedding, 
			@Param("limit") int limit );
}
