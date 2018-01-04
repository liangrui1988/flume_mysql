///*
// * Licensed to the Apache Software Foundation (ASF) under one
// * or more contributor license agreements.  See the NOTICE file
// * distributed with this work for additional information
// * regarding copyright ownership.  The ASF licenses this file
// * to you under the Apache License, Version 2.0 (the
// * "License"); you may not use this file except in compliance
// * with the License.  You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing,
// * software distributed under the License is distributed on an
// * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// * KIND, either express or implied.  See the License for the
// * specific language governing permissions and limitations
// * under the License.
// */
//package com.flume.dome.serializer;
//
//import com.google.common.base.Charsets;
//import org.apache.flume.Context;
//import org.apache.flume.Event;
//import org.apache.flume.FlumeException;
//import org.apache.flume.conf.ComponentConfiguration;
//import org.apache.flume.sink.hbase.AsyncHbaseEventSerializer;
//import org.apache.flume.sink.hbase.SimpleHbaseEventSerializer.KeyType;
//import org.apache.hadoop.hbase.client.Put;
//import org.apache.flume.sink.hbase.SimpleRowKeyGenerator;
//import org.hbase.async.AtomicIncrementRequest;
//import org.hbase.async.PutRequest;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.ArrayList;
//import java.util.List;
///**
// * 实现自定义日志转换
// * <p>
// * sink{@ org.apache.flume.sink.hbase.SimpleAsyncHbaseEventSerializer}
// * 
// * @author rui
// * @date 2017/10/11
// */
//public class ExtSimpleAsyncHbaseEventSerializer implements AsyncHbaseEventSerializer {
//	private static Logger LOG = LoggerFactory.getLogger(ExtSimpleAsyncHbaseEventSerializer.class);
//	private byte[] table;
//	private byte[] cf;
//	private byte[] payload;
//	private byte[] payloadColumn;
//	private byte[] incrementColumn;
//	private String rowPrefix;
//	private byte[] incrementRow;
//	private KeyType keyType;
//
//	@Override
//	public void initialize(byte[] table, byte[] cf) {
//		this.table = table;
//		this.cf = cf;
//	}
//
//	@Override
//	public List<PutRequest> getActions() {
//		List<PutRequest> actions = new ArrayList<PutRequest>();
//		if (payloadColumn != null) {
//			byte[] rowKey;
//			try {
//				switch (keyType) {
//				case TS:
//					rowKey = SimpleRowKeyGenerator.getTimestampKey(rowPrefix);
//					break;
//				case TSNANO:
//					rowKey = SimpleRowKeyGenerator.getNanoTimestampKey(rowPrefix);
//					break;
//				case RANDOM:
//					rowKey = SimpleRowKeyGenerator.getRandomKey(rowPrefix);
//					break;
//				default:
//					rowKey = SimpleRowKeyGenerator.getUUIDKey(rowPrefix);
//					break;
//				}
//				// 这里改成拆分后的k=v存储
//				String content = new String(payload, "utf-8");
//				LOG.debug("content>>>:{}", content);
//				// 按原日志拆分后 存回集合
//				String[] kv1 = content.split("`");
//				for (String str : kv1) {
//					String[] kv2 = str.split("=");
//					if (kv2 != null && kv2.length == 2) {
//						byte[] payloadColumn_conev = kv2[0].getBytes(Charsets.UTF_8);
//						byte[] payload_conev = kv2[1].getBytes(Charsets.UTF_8);
//						LOG.debug("rowKey>:{},payloadColumn>:{},payload>:{}",
//								new Object[] {new String(rowKey), new String(payloadColumn_conev), new String(payload_conev) });
//						PutRequest putRequest = new PutRequest(table, rowKey, cf, payloadColumn_conev, payload_conev);
//						actions.add(putRequest);
//					}
//				}
//				//服务器
//				PutRequest putRequest = new PutRequest(table, rowKey, cf, "server_id".getBytes(Charsets.UTF_8), rowPrefix.getBytes(Charsets.UTF_8));
//				actions.add(putRequest);
//		
//				// PutRequest putRequest = new PutRequest(table, rowKey, cf,
//				// payloadColumn, payload);
//				// actions.add(putRequest);
//			} catch (Exception e) {
//				throw new FlumeException("Could not get row key!", e);
//			}
//		}
//		return actions;
//	}
//
//	public List<AtomicIncrementRequest> getIncrements() {
//		List<AtomicIncrementRequest> actions = new ArrayList<AtomicIncrementRequest>();
//		if (incrementColumn != null) {
//			AtomicIncrementRequest inc = new AtomicIncrementRequest(table, incrementRow, cf, incrementColumn);
//			actions.add(inc);
//		}
//		return actions;
//	}
//
//	@Override
//	public void cleanUp() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void configure(Context context) {
//		String pCol = context.getString("payloadColumn", "pCol");
//		String iCol = context.getString("incrementColumn", "iCol");
//		rowPrefix = context.getString("rowPrefix", "default");
//		String suffix = context.getString("suffix", "uuid");
//		if (pCol != null && !pCol.isEmpty()) {
//			if (suffix.equals("timestamp")) {
//				keyType = KeyType.TS;
//			} else if (suffix.equals("random")) {
//				keyType = KeyType.RANDOM;
//			} else if (suffix.equals("nano")) {
//				keyType = KeyType.TSNANO;
//			} else {
//				keyType = KeyType.UUID;
//			}
//			payloadColumn = pCol.getBytes(Charsets.UTF_8);
//		}
//		if (iCol != null && !iCol.isEmpty()) {
//			incrementColumn = iCol.getBytes(Charsets.UTF_8);
//		}
//		incrementRow = context.getString("incrementRow", "incRow").getBytes(Charsets.UTF_8);
//	}
//
//	@Override
//	public void setEvent(Event event) {
//		this.payload = event.getBody();
//	}
//
//	@Override
//	public void configure(ComponentConfiguration conf) {
//		// TODO Auto-generated method stub
//	}
//
//}
