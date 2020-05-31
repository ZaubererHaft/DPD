select * 
from class c1 
where exists (select * 
              from subclass s 
              where c1.id = s.target_id and exists (select * 
                                                    from association a 
                                                    where a.source_id = s.source_id and a.target_id = c1.id));