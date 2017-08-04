// **********************************************************************
// This file was generated by a TARS parser!
// TARS version 1.0.1.
// **********************************************************************

package com.king.reading.ddb;

import com.qq.tars.protocol.util.*;
import com.qq.tars.protocol.annotation.*;
import com.qq.tars.protocol.tars.*;
import com.qq.tars.protocol.tars.annotation.*;

@TarsStruct
public class GetReadAfterMeGameBoardResponse {

	@TarsStructProperty(order = 0, isRequire = true)
	public java.util.List<Player> players = null;

	public java.util.List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(java.util.List<Player> players) {
		this.players = players;
	}

	public GetReadAfterMeGameBoardResponse() {
	}

	public GetReadAfterMeGameBoardResponse(java.util.List<Player> players) {
		this.players = players;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + TarsUtil.hashCode(players);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof GetReadAfterMeGameBoardResponse)) {
			return false;
		}
		GetReadAfterMeGameBoardResponse other = (GetReadAfterMeGameBoardResponse) obj;
		return (
			TarsUtil.equals(players, other.players) 
		);
	}

	public void writeTo(TarsOutputStream _os) {
		_os.write(players, 0);
	}

	static java.util.List<Player> cache_players;
	static { 
		cache_players = new java.util.ArrayList<Player>();
		Player var_30 = new Player();
		cache_players.add(var_30);
	}

	public void readFrom(TarsInputStream _is) {
		this.players = (java.util.List<Player>) _is.read(cache_players, 0, true);
	}

}
