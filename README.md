# DB_MENUAL
1. 데이터베이스 정리 및 스윙을 이용한 DB연동.


# mybatis 각종 검색 구문.
 oracle
 ``
 LIKE '%%'
 LIKE '%' || #{searchValue} || '%'
 
 ``
 
 mysql
 ``
 title like CONCAT ('%','','%')
 title like CONCAT('%', #{keyword}, '%')
 ``
 
 postsql
 
 ``
 LIKE '%%'
 where userid like '%'||#{keyword}||'%'
 ``
 
 
 