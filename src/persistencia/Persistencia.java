package persistencia;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Persistencia{

	public void escribir(Object obj, String nombreArchivo) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("datos/" + nombreArchivo));
			oos.writeObject(obj);
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public Object leer(String nombreArchivo) {
		Object obj = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("datos/" + nombreArchivo));
			obj = ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return obj;
	}
}