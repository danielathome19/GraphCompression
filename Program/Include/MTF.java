package Program.Include;
import java.util.ArrayList;

public class MTF {

	public static ArrayList<String> encode(ArrayList<String> str, ArrayList<String> sigma) {
		var encoded = new ArrayList<Integer>();
		var s = new StringBuilder(sigma.size());
		for (String tstr : sigma) s.append(tstr);
		for (String tstr : str) {
			int i = s.indexOf(tstr);
			encoded.add(i);
			s = s.deleteCharAt(i).insert(0, tstr);
		}
		var mtf = new ArrayList<String>();
		for (int i : encoded) mtf.add(Integer.toString(i));
		return mtf;
	}

	public static ArrayList<String> decode(ArrayList<String> mtf, ArrayList<String> sigma) {
		var decoded = new ArrayList<String>(mtf.size());
		var s = new StringBuilder(sigma.size());
		for (String tstr : sigma) s.append(tstr);
		for (String ix : mtf) {
			int i = Integer.parseInt(ix);
			String tstr = Character.toString(s.charAt(i));
			decoded.add(tstr);
			s = s.deleteCharAt(i).insert(0, tstr);
		}
		return decoded;
	}
}
