grammar HelloWorld;
greeting: 'Hello' ID;
ID: [a-z]+;
WS: [ \t\r\n]+ -> skip;