grammar SGL;

sheet   : row+;
row     : cell+ NEWLINE;
cell    : DIGITS CONTENT;

NEWLINE  : [\r\n]+; 
DIGITS   : [0-9]+;
CONTENT  : '|' .*? '|';
