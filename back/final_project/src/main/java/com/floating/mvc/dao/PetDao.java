package com.floating.mvc.dao;

public interface PetDao {
	// pet 생성
	int insertPet(String userId);
	// pet 경험치 증가
	int increaseScore(String userId);
	// pet 경험치 감소
	int decreaseScore(String userId);
}
