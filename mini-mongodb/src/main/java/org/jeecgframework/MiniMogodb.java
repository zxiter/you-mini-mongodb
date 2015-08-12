package org.jeecgframework;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.google.gson.Gson;

public class MiniMogodb {
	private static final String _uuid = "_uuid";

	/**
	 * 获取表的全部数据
	 * 
	 * @param path
	 * @param tablename
	 * @return
	 * @throws IOException
	 * @throws JDOMException
	 * @throws Exception
	 */
	public List<Map> loadTableDatas(String path, String tablename) {
		List<Map> l = null;
		try {
			Gson gson = new Gson();
			FileInputStream file = new FileInputStream(path);
			SAXBuilder saxBuilder = new SAXBuilder();
			Document document = saxBuilder.build(file);
			Element root = document.getRootElement();

			l = new ArrayList();
			List<Element> list = root.getChildren();
			for (Element x : list) {
				if (x.getAttributeValue("name").equals(tablename)) {
					List<Element> al = x.getChildren();
					for (Element s : al) {
						String data = s.getText();
						if (data == null) {
							break;
						}
						Map mp = gson.fromJson(data, Map.class);
						l.add(mp);
					}
				}
			}
		} catch (IOException e) {
			System.out.println("严重：读取xml有误!");
		} catch (JDOMException e) {
			System.out.println("严重：xml输出有误!");
		}

		return l;
	}

	/**
	 * 表插入数据
	 * 
	 * @param path
	 * @param tablename
	 * @param po
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	public boolean addData(String path, String tablename, Object po)
			throws JDOMException, IOException {
		boolean flag = false;
		try {
			Gson gson = new Gson();
			FileInputStream file = new FileInputStream(path);
			SAXBuilder saxBuilder = new SAXBuilder();
			Document document = saxBuilder.build(file);
			Element root = document.getRootElement();

			List<Element> list = root.getChildren();
			for (Element x : list) {
				if (x.getAttributeValue("name").equals(tablename)) {
					Map base = new HashMap();
					base.put(_uuid, UUID.randomUUID().toString());
					Element data = new Element("data");
					String json = gson.toJson(po);
					Map mp = gson.fromJson(json, Map.class);
					base.putAll(mp);
					data.addContent(gson.toJson(base));
					x.addContent(data);
				}
			}
			XMLOutputter out = new XMLOutputter();
			out.output(document, new FileOutputStream(path));
			flag = true;
			System.out
					.println("----------insert --- data --- success----------------------");
		} catch (IOException e) {
			System.out.println("严重：读取xml有误!");
		} catch (JDOMException e) {
			System.out.println("严重：xml解析错误!");
		}

		return flag;
	}

	/**
	 * 表修改数据
	 * 
	 * @param path
	 * @param tablename
	 * @param po
	 * @throws JDOMException
	 * @throws IOException
	 */
	public void updateData(String path, String tablename, Object po) {
		try {
			FileInputStream file = new FileInputStream(path);
			SAXBuilder saxBuilder = new SAXBuilder();
			Document document = saxBuilder.build(file);
			Element root = document.getRootElement();
			Gson gson = new Gson();

			List<Element> list = root.getChildren();
			for (Element x : list) {
				if (x.getAttributeValue("name").equals(tablename)) {
					List<Element> al = x.getChildren();
					for (Element s : al) {
						// 如果定位Data[通过UUID唯一标示]
						Map mp = gson.fromJson(s.getText(), Map.class);
						Map newmp = gson.fromJson(gson.toJson(po), Map.class);
						if (mp.get(_uuid).equals(newmp.get(_uuid))) {
							mp.putAll(newmp);
							Element data = new Element("data");
							data.setText(gson.toJson(mp));
							x.removeContent(s);
							x.addContent(data);
							break;
						}
					}
				}
			}
			XMLOutputter out = new XMLOutputter();
			out.output(document, new FileOutputStream(path));
			System.out
					.println("----------update --- data --- success----------------------");
		} catch (IOException e) {
			System.out.println("严重：读取xml有误!");
		} catch (JDOMException e) {
			System.out.println("严重：xml解析错误!");
		}

	}

	/**
	 * 表删除数据
	 * 
	 * @param path
	 * @param tablename
	 * @param po
	 * @throws JDOMException
	 * @throws IOException
	 */
	public void deleteData(String path, String tablename, Object po) {
		try {
			FileInputStream file = new FileInputStream(path);
			Gson gson = new Gson();
			SAXBuilder saxBuilder = new SAXBuilder();
			Document document = saxBuilder.build(file);
			Element root = document.getRootElement();
			List<Element> list = root.getChildren();
			for (Element x : list) {
				List<Element> al = x.getChildren();
				if (x.getAttributeValue("name").equals(tablename)) {
					for (Element s : al) {
						// 如果定位Data[通过UUID唯一标示]
						Map mp = gson.fromJson(s.getText(), Map.class);
						Map newmp = gson.fromJson(gson.toJson(po), Map.class);
						if (mp.get(_uuid).equals(newmp.get(_uuid))) {
							x.removeContent(s);
							break;
						}
					}
				}
			}
			XMLOutputter out = new XMLOutputter();
			out.output(document, new FileOutputStream(path));
			System.out
					.println("----------delete --- data --- success----------------------");
		} catch (IOException e) {
			System.out.println("严重：读取xml有误!");
		} catch (JDOMException e) {
			System.out.println("严重：xml解析错误!");
		}

	}

	/**
	 * 创建基本的数据库
	 * 
	 * @param path
	 * @throws
	 * @throws
	 * @throws Exception
	 */
	public void createDataBase(String path) {
		try {
			FileOutputStream file1 = new FileOutputStream(path);
			Document document = new Document();
			Element root = new Element("database");
			Element sort1 = new Element("table");
			sort1.setAttribute("name", "test");
			Element sort2 = new Element("table");
			sort2.setAttribute("name", "system.indexs");
			Element sort3 = new Element("table");
			sort3.setAttribute("name", "system.users");

			root.addContent(sort1);
			root.addContent(sort2);
			root.addContent(sort3);

			document.setRootElement(root);
			XMLOutputter out = new XMLOutputter();
			out.output(document, file1);
			System.out
					.println("----------create---database---success-------------------");
		} catch (FileNotFoundException e) {
			System.out.println("严重：数据库文件不存在!");
		} catch (IOException e) {
			System.out.println("严重：读取xml有误!");
		}

	}

	/**
	 * 根据名字创建数据库
	 * 
	 * @param path
	 * @param dbName
	 * @throws Exception
	 */

	public void createDataBase(String path, String dbName) {
		boolean flag = false;
		try {
			Element sort1 = new Element("table");
			sort1.setAttribute("name", dbName);
			FileInputStream file = new FileInputStream(path);
			SAXBuilder saxBuilder = new SAXBuilder();
			Document document = saxBuilder.build(file);
			Element root = document.getRootElement();
			List<Element> list = root.getChildren();
			for (Element x : list) {
				if (x.getAttributeValue("name").equals(dbName)) {
					System.out.println(dbName + "数据库已存在!");
					flag = false;
				} else {
					flag = true;
				}
			}

			if (flag) {
				root.addContent(sort1);
				document.setRootElement(root);
				XMLOutputter out = new XMLOutputter();
				out.output(document, new FileOutputStream(path));
				System.out
						.println("----------create---database---success-------------------");
			}

		} catch (Exception e) {
			System.out.println("严重：数据库不存在!");
		}

	}

	/**
	 * 查看所有数据库
	 * 
	 * @param path
	 * 
	 */
	public List<String> findDataBase(String path) {
		List<String> dataBaseNames = null;
		try {
			dataBaseNames = new ArrayList<String>();
			FileInputStream file = new FileInputStream(path);
			Gson gson = new Gson();
			SAXBuilder saxBuilder = new SAXBuilder();
			Document document = saxBuilder.build(file);
			Element root = document.getRootElement();
			List<Element> list = root.getChildren();
			for (Element x : list) {
				dataBaseNames.add(x.getAttributeValue("name"));
			}
			XMLOutputter out = new XMLOutputter();
			out.output(document, new FileOutputStream(path));
			System.out
					.println("----------find--- database --- success----------------------");
		} catch (IOException e) {
			System.out.println("严重：读取xml有误!");
		} catch (JDOMException e) {
			System.out.println("严重：xml解析错误!");
		}

		return dataBaseNames;
	}
}