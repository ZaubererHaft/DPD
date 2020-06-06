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
SELECT distinct (i.id), i.name, i.type, c2.id, c2.name, c2.type
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
SELECT distinct (c1.id), c1.name, c1.type, c2.id, c2.name, c2.type
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
SELECT distinct (c.id), c.name, c.type, c2.id, c2.name, c2.type
FROM classifier c JOIN derivation d1 ON d1.target_id = c.id 
                  JOIN method m ON m.classifier_id = d1.source_id
                  JOIN classifier c2 ON c2.type IN ('DEFAULT', 'ABSTRACT')
                  JOIN methodinvocation mi ON mi.classifier_id = c2.id AND mi.method_id = m.id
WHERE c.type = 'INTERFACE';


-- bridge 
SELECT * FROM classifier c1 WHERE c1.type IN ('ABSTRACT') AND EXISTS (SELECT * FROM derivation d1 WHERE d1.target_id = c1.id
                                                                     AND EXISTS (SELECT * FROM method m WHERE m.classifier_id = c1.id
                                                                     AND EXISTS (SELECT * FROM classifier c2 WHERE  c2.type IN ('ABSTRACT')
                                                                     AND EXISTS (SELECT * FROM methodinvocation mi WHERE mi.method_id=m.id AND mi.classifier_id = c2.id
                                                                     AND EXISTS (SELECT * FROM derivation d2 WHERE d2.target_id = c2.id)))));
-- bridge as join
SELECT distinct (c1.id), c1.name, c1.type, c2.id, c2.name, c2.type FROM classifier c1 JOIN derivation d1 ON d1.target_id = c1.id
                            JOIN method m ON m.classifier_id = c1.id
                            JOIN classifier c2 ON c2.type IN ('ABSTRACT')
                            JOIN methodinvocation mi ON mi.method_id=m.id AND mi.classifier_id = c2.id
                            JOIN derivation d2 ON d2.target_id = c2.id
WHERE c1.type IN ('ABSTRACT');

--decorator
SELECT * FROM classifier c1 WHERE c1.type IN ('ABSTRACT') AND EXISTS (SELECT * FROM classifier c2 WHERE c2.type IN ('ABSTRACT') 
                                                          AND EXISTS (SELECT * FROM derivation d1 WHERE d1.target_id = c1.id AND d1.source_id = c2.id
                                                          AND EXISTS (SELECT * FROM association a WHERE a.source_id = c2.id AND a.target_id = c1.id
                                                          AND EXISTS (SELECT * FROM derivation d4 WHERE d4.target_id = c2.id
                                                          AND EXISTS (SELECT * FROM derivation d2 WHERE d2.target_id = c1.id AND d2.id <> d1.id
                                                          AND NOT EXISTS (SELECT * FROM derivation d3 WHERE d2.source_id = d3.target_id))))));
-- deorator as join
SELECT distinct (c1.id), c1.name, c1.type, c2.id, c2.name, c2.type FROM classifier c1 JOIN  classifier c2 ON c2.type IN ('ABSTRACT') 
                            JOIN derivation d1 ON d1.target_id = c1.id AND d1.source_id = c2.id
                            JOIN association a ON a.source_id = c2.id AND a.target_id = c1.id
                            JOIN derivation d4 ON d4.target_id = c2.id
                            JOIN derivation d2 ON d2.target_id = c1.id AND d2.id <> d1.id AND NOT EXISTS (SELECT * FROM derivation d3 WHERE d2.source_id = d3.target_id)
WHERE c1.type IN ('ABSTRACT');

-- facade:
-- sehr unkonkret, (ein subsystem wird nur über ein interface angesprochen), daher ausgelassen

-- flyweight
-- wieder zu konkret, fordert sehr spezielle art der objektgenerierung, daher ausgelassen

-- composite (methodenaufrauf auf parent gespart)
SELECT * FROM classifier c1 WHERE c1.type IN ('ABSTRACT') AND EXISTS (SELECT * FROM classifier c2 WHERE c2.type IN ('DEFAULT')  
                                                          AND EXISTS (SELECT * FROM derivation d1 WHERE d1.target_id = c1.id AND d1.source_id = c2.id
                                                          AND EXISTS (SELECT * FROM association a WHERE a.source_id = c2.id AND a.target_id = c1.id AND a.uppermultiplicity = '*'
                                                          AND EXISTS (SELECT * FROM derivation d2 WHERE d2.target_id = c1.id AND d2.id <> d1.id
                                                          AND NOT EXISTS (SELECT * FROM derivation d3 WHERE d2.source_id = d3.target_id)))));

-- composite as join
SELECT distinct (c1.id), c1.name, c1.type, c2.id, c2.name, c2.type FROM classifier c1 JOIN classifier c2 ON c2.type IN ('DEFAULT')  
                            JOIN derivation d1 ON d1.target_id = c1.id AND d1.source_id = c2.id
                            JOIN association a ON a.source_id = c2.id AND a.target_id = c1.id AND a.uppermultiplicity = '*'
                            JOIN derivation d2 ON d2.target_id = c1.id AND d2.id <> d1.id AND NOT EXISTS (SELECT * FROM derivation d3 WHERE d2.source_id = d3.target_id)

WHERE c1.type IN ('ABSTRACT');

--command
SELECT * FROM classifier c1 WHERE c1.type IN ('ABSTRACT','DEFAULT') AND EXISTS (SELECT * FROM classifier c2 WHERE c2.type IN ('INTERFACE')
                                                                    AND EXISTS (SELECT * FROM method m1 WHERE m1.classifier_id = c1.id
                                                                    AND EXISTS (SELECT * FROM methodinvocation mi1 WHERE mi1.method_id = m1.id AND mi1.classifier_id = c2.id
                                                                    AND EXISTS (SELECT * FROM derivation d1 WHERE d1.target_id = c2.id
                                                                    AND EXISTS (SELECT * FROM method m2 WHERE m2.classifier_id = d1.source_id
                                                                    AND EXISTS (SELECT * FROM methodinvocation mi2 WHERE mi2.method_id = m2.id
                                                                    AND EXISTS (SELECT * FROM classifier c3 WHERE c3.type IN ('ABSTRACT','DEFAULT') AND mi2.classifier_id = c3.id)))))));


-- command as join
SELECT distinct (c1.id), c1.name, c1.type, c2.*, c3.*  FROM classifier c1 JOIN classifier c2 ON c2.type IN ('INTERFACE')
                            JOIN method m1 ON m1.classifier_id = c1.id
                            JOIN methodinvocation mi1 ON mi1.method_id = m1.id AND mi1.classifier_id = c2.id
                            JOIN derivation d1 ON d1.target_id = c2.id
                            JOIN method m2 ON m2.classifier_id = d1.source_id
                            JOIN methodinvocation mi2 ON mi2.method_id = m2.id
                            JOIN classifier c3 ON c3.type IN ('ABSTRACT','DEFAULT') AND mi2.classifier_id = c3.id
WHERE c1.type IN ('ABSTRACT','DEFAULT');

                                                                   
