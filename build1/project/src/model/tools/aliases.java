package model.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**находит возможные заголовки, находит синонимы для заголовков столбцов
 * @author Клим
 *TODO кодировки...
 */
public class aliases {
	private static final  Map<aliasesNames, String[]> ffs;
							static {
								ffs = new HashMap<aliasesNames, String[]>();
								
								ffs.put(aliasesNames.NAME,new String[] {
										"имя",
										"название",
										"наименование",
										"товар"
								} );
								ffs.put(aliasesNames.CROSSNAME,new String[] {
										"артикул",
										"кросс",
										"кроссы"
								});
								ffs.put(aliasesNames.SUPPLIER,new String[] {
										"поставщик"
										
								});
								ffs.put(aliasesNames.BUYER,new String[] {
										"покупатель",
										
								});
								ffs.put(aliasesNames.PRODUCER,new String[] {
										"производитель",
										
								});
								ffs.put(aliasesNames.SELLPRICE,new String[] {
										"цена продажи",
										"продажа",
										"цена продажн",
										
										
								});
								ffs.put(aliasesNames.BUYPRICE,new String[] {
										"цена закупки",
										"закупка",
										"цена зак",
										
										
								});
								ffs.put(aliasesNames.DESCRIPTION,new String[] {
										"описание",
										"комментарий",
										"дополнительно",
										
										
								});
								ffs.put(aliasesNames.DATE,new String[] {
										"дата",
										"время"
								});
								ffs.put(aliasesNames.QUANTATY,new String[] {
										"кол-во",
										"количество",
										"кол во",
										
										
								});
								ffs.put(aliasesNames.OPERATION,new String[] {
										"операция"});
								ffs.put(aliasesNames.RECEIPTNUMBER,new String[] {
								"номер чека"});
								
								
	}
	private Map<aliasesNames,Integer> result;
	public aliases( ArrayList<String> val){
		result = new HashMap<aliasesNames,Integer>();
		int i = 0;
		for(String s:val) {
			s  = staticTools.strObfusc(s);
			//ffs.entrySet().i
			for (Entry<aliasesNames, String[]> keyval : ffs.entrySet()) {
				boolean exit = false;
				String[] entryval = keyval.getValue();
				for (String stringval : entryval) {
					stringval  = staticTools.strObfusc(stringval);
					if (s.equals(stringval)) {
						result.put(keyval.getKey(), i);
						exit = true;
						break;
					}
				}
				if (exit) {
					break;
				}
			}
			i++;
		}
		
	}
	
	public Integer get(aliasesNames val) {
		return result.get(val);
		
	}
	public static enum aliasesNames{
		NAME,
		CROSSNAME,
		SUPPLIER,
		BUYER,
		PRODUCER,
		SELLPRICE,
		BUYPRICE,
		DESCRIPTION,
		DATE,
		QUANTATY,
		OPERATION, RECEIPTNUMBER
	};
}
