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
public class Player {

	@TarsStructProperty(order = 0, isRequire = true)
	public int rank = 0;
	@TarsStructProperty(order = 1, isRequire = true)
	public String name = "";
	@TarsStructProperty(order = 2, isRequire = true)
	public int otainedStar = 0;
	@TarsStructProperty(order = 3, isRequire = true)
	public int completedMission = 0;

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOtainedStar() {
		return otainedStar;
	}

	public void setOtainedStar(int otainedStar) {
		this.otainedStar = otainedStar;
	}

	public int getCompletedMission() {
		return completedMission;
	}

	public void setCompletedMission(int completedMission) {
		this.completedMission = completedMission;
	}

	public Player() {
	}

	public Player(int rank, String name, int otainedStar, int completedMission) {
		this.rank = rank;
		this.name = name;
		this.otainedStar = otainedStar;
		this.completedMission = completedMission;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + TarsUtil.hashCode(rank);
		result = prime * result + TarsUtil.hashCode(name);
		result = prime * result + TarsUtil.hashCode(otainedStar);
		result = prime * result + TarsUtil.hashCode(completedMission);
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
		if (!(obj instanceof Player)) {
			return false;
		}
		Player other = (Player) obj;
		return (
			TarsUtil.equals(rank, other.rank) &&
			TarsUtil.equals(name, other.name) &&
			TarsUtil.equals(otainedStar, other.otainedStar) &&
			TarsUtil.equals(completedMission, other.completedMission) 
		);
	}

	public void writeTo(TarsOutputStream _os) {
		_os.write(rank, 0);
		_os.write(name, 1);
		_os.write(otainedStar, 2);
		_os.write(completedMission, 3);
	}


	public void readFrom(TarsInputStream _is) {
		this.rank = _is.read(rank, 0, true);
		this.name = _is.readString(1, true);
		this.otainedStar = _is.read(otainedStar, 2, true);
		this.completedMission = _is.read(completedMission, 3, true);
	}

}