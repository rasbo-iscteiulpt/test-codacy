package cliente;

import java.util.ArrayList;
import javax.swing.AbstractListModel;

import common.Resposta;

@SuppressWarnings("serial")
public class NewsModel extends AbstractListModel<Resposta>{
	
	ArrayList<Resposta> items = new ArrayList<Resposta>();
	
	@Override
	public Resposta getElementAt(int i) {
		return items.get(i);
	}

	@Override
	public int getSize() {
		return items.size();
	}

	public void fill(ArrayList<Resposta> list) {
		items.clear();
		for(Resposta r : list){
			items.add(r);
		}
		fireContentsChanged(this, 0, items.size());
	}
	
	public void clear() {
		items.clear();
		fireContentsChanged(this, 0, items.size());
	}
}
