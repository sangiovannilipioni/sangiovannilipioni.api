grammar SGL;

sheet   : cell+;
cell    : DIGITS CONTENT;

NEWLINE  : [\r\n]+ -> skip; 
DIGITS   : [0-9]+;
CONTENT  : '|' .*? '|';
