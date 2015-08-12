package org.jeecgframework;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class TestMiniMogodb {
	private static final String uuid = "43e70723-8d08-4aba-bd2b-8e9722c4fee6";
	private static final String database_name = "you_mini-mongodb.xml";

	/**
	 * 创建默认数据库
	 */
	@Test
	public void initDatabse() {
		try {
			MiniMogodb dao = new MiniMogodb();
			dao.createDataBase(database_name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建数据库
	 */
	@Test
	public void createDatabse() {
		try {
			MiniMogodb dao = new MiniMogodb();
			dao.createDataBase(database_name, "you1");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查看数据库
	 */
	@Test
	public void findDatabse() {
		try {
			MiniMogodb dao = new MiniMogodb();
			List<String> list = dao.findDataBase(database_name);

			for (String dataBaseName : list) {
				System.out.println("dataBaseName:" + dataBaseName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 插入数据
	 */
	@Test
	public void insertData() {
		Person po = new Person();
		po.setName("liu");
		po.setAge(20);
		po.setMoney(2000.98);
		po.setSex("男");
		try {
			MiniMogodb dao = new MiniMogodb();
			dao.addData(database_name, "you", po);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 修改数据
	 */
	@Test
	public void updateData() {
		Person po = new Person();
		po.set_uuid(uuid);
		po.setName("ilike");
		po.setAge(28);
		try {
			MiniMogodb dao = new MiniMogodb();
			dao.updateData(database_name, "you", po);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除数据
	 */
	@Test
	public void deleteData() {
		Person po = new Person();
		po.set_uuid(uuid);
		try {
			MiniMogodb dao = new MiniMogodb();
			dao.deleteData(database_name, "you", po);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取所有数据
	 */
	@Test
	public void listAll() {
		try {
			MiniMogodb dao = new MiniMogodb();
			List<Map> list = dao.loadTableDatas(database_name, "you");
			System.out.println(list.toString());
			System.out.println(list.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
