/*
 *    This file is part of mlDHT.
 * 
 *    mlDHT is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 2 of the License, or
 *    (at your option) any later version.
 * 
 *    mlDHT is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 * 
 *    You should have received a copy of the GNU General Public License
 *    along with mlDHT.  If not, see <http://www.gnu.org/licenses/>.
 */
package lbms.plugins.mldht.kad.messages;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import lbms.plugins.mldht.kad.DHT;
import lbms.plugins.mldht.kad.Key;

/**
 * @author Damokles
 *
 */
public class AnnounceRequest extends AbstractLookupRequest {

	protected int		port;
	boolean				isSeed;
	protected byte[]	token;
	ByteBuffer			name;

	/**
	 * @param id
	 * @param info_hash
	 * @param port
	 * @param token
	 */
	public AnnounceRequest (Key info_hash, int port, byte[] token) {
		super(info_hash, Method.ANNOUNCE_PEER);
		this.port = port;
		this.token = token;
	}

	public boolean isSeed() {
		return isSeed;
	}

	public void setSeed(boolean isSeed) {
		this.isSeed = isSeed;
	}

	@Override
	public void apply (DHT dh_table) {
		dh_table.announce(this);
	}
	
	
	@Override
	public Map<String, Object> getInnerMap() {
		Map<String, Object> inner = new TreeMap<>();

		inner.put("id", id.getHash());
		inner.put("info_hash", target.getHash());
		inner.put("port", port);
		inner.put("token", token);
		inner.put("seed", Long.valueOf(isSeed ? 1 : 0));
		if(name != null)
			inner.put("name", name);

		return inner;
	}
	
	public void setName(ByteBuffer name) {
		this.name = name;
	}
	
	public Optional<ByteBuffer> getName() {
		return Optional.ofNullable(name).map(ByteBuffer::asReadOnlyBuffer);
	}
	
	public Optional<String> getNameUTF8() {
		return Optional.ofNullable(name).map(n -> StandardCharsets.UTF_8.decode(n.slice()).toString());
	}


	/**
	 * @return the token
	 */
	public byte[] getToken () {
		return token;
	}
	
	public int getPort() {
		return port;
	}
	
	@Override
	public String toString() {
		return super.toString() + " seed:" + isSeed + " token:" + token.length + " port:" + port + " name:" + getNameUTF8().orElse("") ;
	}

	@Override
	protected String targetBencodingName() {
		return "info_hash";
	}
}
