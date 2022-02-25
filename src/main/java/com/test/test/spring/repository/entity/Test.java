/**
 *
 */
package com.test.test.spring.repository.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author test
 *
 */
@Getter
@Setter
public class Test {

	private String MENUID;
	private String KOR_NAME;
	private String ENG_NAME;
	private String CD_TP1;
	private String CD_V_EXPLAIN;
	private String FK_CD_TP;
	private String SEARCH_SORT_SEQ;
	private String DEPTH;
	private String MENUSHOWYN;
	private String USECOMCODE;
	private String MENU_METAS;
	

	

	public Test() {}

//	public Test(String userName) {
//		this.userName = userName;
//	}
//
//	@Override
//	public boolean equals(Object other) {
//		if (other != null && other instanceof User) {
//			if (((User) other).getId() == this.id) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	@Override
//	public int hashCode() {
//		return (userName == null ? userName.length() : 0);
//	}

//	@Override
//	public String toString() {
//		return "MENUID[" + MENUID +"] userName[" + userName + "]";
//	}

}