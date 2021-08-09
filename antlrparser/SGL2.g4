grammar SGL;

total   : sheet+;
sheet   : sheetid row+;
sheetid : '-1' CONTENT NEWLINE;
row     : cell+ NEWLINE;
cell    : DIGITS CONTENT;

NEWLINE  : [\r\n]+; 
DIGITS   : [0-9]+;
CONTENT  : '|' .*? '|';
