grammar csv;
file: line*;
line: Num ',' Num ',' Txt ',' Num;
Num: '-'?[0-9]+;
Txt: '"'[a-z]+'"';
WS: [ \t\r\n]+ -> skip;