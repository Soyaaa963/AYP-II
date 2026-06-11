package maps;

import net.datastructures.ChainHashMap;
import net.datastructures.Entry;
import net.datastructures.Map;
import net.datastructures.ProbeHashMap;
import net.datastructures.UnsortedTableMap;

public class Test {

	public static void main(String[] arg) {
		
	Alumno a1 = new Alumno("juan",123);
	Alumno a2 = new Alumno("ana",321);
	Alumno a3 = new Alumno("maria",222);
	Alumno a4 = new Alumno("pedro",333);
	
	//Map<Integer,Alumno> map = new UnsortedTableMap<Integer,Alumno>();
	//Map<Integer,Alumno> map = new ChainHashMap<Integer,Alumno>();
	Map<Integer,Alumno> map = new ProbeHashMap<Integer,Alumno>();
	
	map.put(123, a1);
	map.put(321, a2);
	map.put(222, a3);
	map.put(333, a4);
	
	
	System.out.println(map.get(222));

	map.remove(222);
	
	System.out.println(map.get(222));
	
	
	for (int  i: map.keySet())	
		System.out.println(i);
	
	for (Alumno a: map.values())
		System.out.println(a);
	
	for (Entry<Integer, Alumno> e:map.entrySet())
		System.out.println(e.getKey() + "" + e.getValue());
	
	}
}
