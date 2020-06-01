select * 
from class c1 
where exists (select * 
              from subclass s 
              where c1.id = s.target_id and exists (select * 
                                                    from association a 
                                                    where a.source_id = s.source_id and a.target_id = c1.id));


select c1.name, c1.type, c2.name, c2.type from derivation d join classifier c1 on c1.id = d.source_id join classifier c2 on c2.id = d.target_id


	"query" : "SELECT c1 FROM Classifier c1 WHERE c1.type IN (ClassifierType.ABSTRACT,ClassifierType.DEFAULT) AND EXISTS (SELECT d FROM Derivation d WHERE c1 = d.target AND EXISTS (SELECT a FROM Association a WHERE a.source = d.source AND a.target = c1))"
