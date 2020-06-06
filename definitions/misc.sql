-- join derivations and classes
select c1.name, c1.type, c2.name, c2.type from derivation d join classifier c1 on c1.id = d.source_id join classifier c2 on c2.id = d.target_id
-- join method an class
select m.name, m.isstatic, c.name from method m join classifier c on m.classifier_id = c.id;
--join method invocation, class and method
select * from methodinvocation mi join classifier c on c.id = mi.classifier_id join method m on mi.method_id = m.id join classifier c2 on m.classifier_id = c2.id;


-- proxy
SELECT * FROM classifier i WHERE i.type = 'INTERFACE' AND EXISTS (SELECT * FROM derivation d1 WHERE d1.target_id = i.id 
                                                      AND EXISTS (SELECT * FROM method m WHERE m.classifier_id = d1.source_id
                                                      AND EXISTS (SELECT * FROM method m2 WHERE m2.name = m.name AND m2.classifier_id = i.id
                                                      AND EXISTS (SELECT * FROM methodinvocation mi WHERE mi.classifier_id = i.id AND mi.method_id = m.id))));
--proxy as join
SELECT c2.*, i.*
FROM classifier i JOIN derivation d1 ON d1.target_id = i.id 
                  JOIN classifier c2 ON d1.source_id = c2.id
                  JOIN method m ON m.classifier_id = d1.source_id
                  JOIN method m2 ON (m2.name = m.name AND m2.classifier_id = i.id)
                  JOIN methodinvocation mi ON (mi.classifier_id = i.id AND mi.method_id = m.id)
WHERE i.type = 'INTERFACE';

-- strategy
SELECT * FROM classifier c1 WHERE c1.type IN ('ABSTRACT', 'DEFAULT') AND EXISTS (SELECT * FROM classifier c2 WHERE c2.type = 'INTERFACE'
                                                                     AND EXISTS (SELECT * FROM method m WHERE m.classifier_id = c1.id 
                                                                     AND EXISTS (SELECT * FROM methodinvocation mi WHERE mi.classifier_id = c2.id AND mi.method_id = m.id)));

-- strategy as join
SELECT c1.* as test1, c2.* as test2
FROM classifier c1 JOIN method m ON m.classifier_id = c1.id
                   JOIN methodinvocation mi ON mi.method_id = m.id 
                   JOIN classifier c2 ON mi.classifier_id = c2.id 
WHERE c1.type IN ('ABSTRACT', 'DEFAULT') AND c2.type = 'INTERFACE';

-- adapter
SELECT * FROM classifier c WHERE c.type = 'INTERFACE' AND EXISTS (SELECT * FROM derivation d1 WHERE d1.target_id = c.id 
                                                      AND EXISTS (SELECT * FROM method m WHERE m.classifier_id = d1.source_id
                                                      AND EXISTS (SELECT * FROM classifier c2 WHERE c2.type IN ('DEFAULT', 'ABSTRACT')
                                                      AND EXISTS (SELECT * FROM methodinvocation mi WHERE mi.classifier_id = c2.id AND mi.method_id = m.id))));
-- adapter as join
SELECT c.*, c2.* 
FROM classifier c JOIN derivation d1 ON d1.target_id = c.id 
                  JOIN method m ON m.classifier_id = d1.source_id
                  JOIN classifier c2 ON c2.type IN ('DEFAULT', 'ABSTRACT')
                  JOIN methodinvocation mi ON mi.classifier_id = c2.id AND mi.method_id = m.id
WHERE c.type = 'INTERFACE';
