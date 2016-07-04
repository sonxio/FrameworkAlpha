不需要考虑把这两个对象单独提出来,放在COMMON里.
一来因为这两个对象中都涉及到当前模块的对象, 如果想做成通用的,要增加额外的复杂度
二来这两个对象都是随NETBEANS PERSISTENCE > JFS PAGES FROM ENTITY CLASSES可以自行生成的, 如果一定要放在COMMON里,每多一个模块会增加不少自己敲入的代码量.


