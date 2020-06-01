-- join derivations and classes
select c1.name, c1.type, c2.name, c2.type from derivation d join classifier c1 on c1.id = d.source_id join classifier c2 on c2.id = d.target_id
-- join method an class
select m.name, m.isstatic, c.name from method m join classifier c on m.classifier_id = c.id;
