package com.inaigem.smatbackend.token;


import com.inaigem.smatbackend.generic.IGenericRepo;

public interface IResetTokenRepo extends IGenericRepo<ResetToken, Integer> {
	
	//from ResetToken rt where rt.token = :?
	ResetToken findByToken(String token);

}
