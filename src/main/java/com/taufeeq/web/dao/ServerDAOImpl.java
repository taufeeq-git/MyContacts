package com.taufeeq.web.dao;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.taufeeq.web.enums.Enum.EnumComparator;
import com.taufeeq.web.enums.Enum.EnumConjunction;
import com.taufeeq.web.enums.Enum.Table;
import com.taufeeq.web.enums.Enum.servers;
import com.taufeeq.web.helper.FieldMapperHelper;
import com.taufeeq.web.model.Server;
import com.taufeeq.web.querybuilder.QueryBuilder;
import com.taufeeq.web.querybuilder.QueryBuilderFactory;

public class ServerDAOImpl implements ServerDAO {

	QueryBuilder queryBuilder;

	@Override
	public boolean checkIfSeverExits(String ipAddress, int port) {
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		try {
			List<Integer> res = queryBuilder.select(servers.server_id).from(Table.servers)
					.where(servers.ip_address, EnumComparator.EQUAL, ipAddress)
					.conjunction(EnumConjunction.AND,servers.port_number, EnumComparator.EQUAL, port).executeSelect(Integer.class, null);

			return !res.isEmpty();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public int addServer(Server server) {
		long time = System.currentTimeMillis() / 1000;
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		try {
			queryBuilder.insert(Table.servers, servers.ip_address, servers.port_number, servers.created_time)
					.values(server.getIpAddress(), server.getPortNumber(), time).executeInsert();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public List<Server> getOtherServers(Server server) {
		Map<String, String> fieldMapping = FieldMapperHelper.getFieldMapping(Table.servers);
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		try {
			List<Server> serverList = queryBuilder.selectAll().from(Table.servers)
					.where(servers.ip_address, EnumComparator.NOT_EQUAL, server.getIpAddress())
					.conjunction(EnumConjunction.OR,servers.port_number, EnumComparator.NOT_EQUAL, server.getPortNumber())
					.executeSelect(Server.class, fieldMapping);

			return serverList;
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@Override
	public List<Server> getAllServers() {
		Map<String, String> fieldMapping = FieldMapperHelper.getFieldMapping(Table.servers);
		queryBuilder = QueryBuilderFactory.getQueryBuilder();
		try {
			List<Server> serverList = queryBuilder.selectAll().from(Table.servers).executeSelect(Server.class,
					fieldMapping);

			return serverList;
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}
}