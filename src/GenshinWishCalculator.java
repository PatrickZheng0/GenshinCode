import java.util.ArrayList;

public class GenshinWishCalculator {

	public static void main(String[] args) {

		ArrayList<Double> patrickPulls = percentPullAll(20, 1, 0, 0, 0, false, 0, "char", 50000);
		Double weps = percentPullWep(68, 1, 0, 50000, true, 0).get(0);
// (numPulls, numChar, numWep, charPity, wepPity, charGuarantee, wepEpitomizePity, "char" or "wep", simAmt)

		Double fourStarPulls = percentPull4Star(62, 3, 0, 50000, true, false);
		int pullNeed = wishFor4Star(80, 3, 0, 20000, true, false);

		
//		System.out.println(patrickPulls);
		System.out.println(weps);
		System.out.println("amount of pulls left is wrong");
		//pity doesn't seem to work well with numpulls left value
		
		
	}
	
	public static ArrayList<Integer> simAll(int numPulls, int numChar, int numWep, int charPity, int wepPity, boolean guaranteeChar, int ePity, String prioritize) {
		ArrayList<Integer> info = new ArrayList<>();
		ArrayList<Integer> sim;
		
		if (prioritize.equalsIgnoreCase("char")) {
			sim = simWep(simChar(numPulls, numChar, charPity, true, guaranteeChar).get(1), numWep, wepPity, true, ePity);
			info.add(sim.get(0));
			info.add(sim.get(3));
		} else if (prioritize.equalsIgnoreCase("wep")) {
			sim = simChar(simWep(numPulls, numWep, wepPity, true, ePity).get(3), numChar, charPity, true, guaranteeChar);
			info.add(sim.get(0));
			info.add(sim.get(1));
		}
		
		return info;
	}
	
	public static ArrayList<Double> percentPullAll(int numPulls, int numChar, int numWep, int charPity, int wepPity, boolean guaranteeChar, int ePity, String prioritize, int simAmt) {
		ArrayList<Double> info = new ArrayList<>();
		ArrayList<Integer> fiveStar = new ArrayList<>();
		ArrayList<Integer> pullsLeft = new ArrayList<>();
		ArrayList<Integer> sim;
		int num = 0;
		double val = 1;
		if (prioritize.equalsIgnoreCase("char")) {
			num = numWep;
		} else if (prioritize.equalsIgnoreCase("wep")) {
			num = numChar;
		} else {
			System.out.println("WRITE 'char' or 'wep' AS THE LAST PARAMETER");
		}
		
		while (simAmt > 0) {
			sim = simAll(numPulls, numChar, numWep, charPity, wepPity, guaranteeChar, ePity, prioritize);
			fiveStar.add(sim.get(0));
			pullsLeft.add(sim.get(1));
			simAmt--;
		}
		
		double count = 0;
		for (int i : fiveStar) {
			if (i >= num) {
				count++;
			}
		}
		
		double sum = 0;
		for (int i : pullsLeft) {
			if (i != 0) {
				sum += i;
			}
		}
		
		if (prioritize.equalsIgnoreCase("char")) {
			val = percentPullChar(numPulls, numChar, charPity, 50000, true, guaranteeChar) / 100;
		} else if (prioritize.equalsIgnoreCase("wep")) {
			val = percentPullWep(numPulls, numWep, wepPity, 50000, true, ePity).get(0) / 100;
		}
		
		info.add(100 * val * count / fiveStar.size());
		
		if (info.get(0) > 99.0) {
			info.add(sum / pullsLeft.size());
		} else {
			info.add(null);
		}
		
		return info;
	}
	
	
	public static int wishFor4Star(double percentage, int num4Star, int pity, int simAmt, boolean want5050, boolean guarantee) {
		
		int pull = 0;
			
		int numPulls = 0;
		
		while (percentage > percentPull4Star(numPulls, num4Star, pity, simAmt, want5050, guarantee)) {
			numPulls += 10;
		}
		
		for (int j = numPulls - 10; j <= numPulls; j++) {
			if (percentPull4Star(j, num4Star, pity, simAmt, want5050, guarantee) > percentage) {
				pull = j;
				break;
			}
		}
		
		return pull;
	}
	
	public static int wishForWep4Star(double percentage, int num4Star, int pity, int simAmt, boolean want5050, boolean guarantee) {
		
		int pull = 0;
			
		int numPulls = 0;
		
		while (percentage > percentPullWep4Star(numPulls, num4Star, pity, simAmt, want5050, guarantee)) {
			numPulls += 10;
		}
		
		for (int j = numPulls - 10; j <= numPulls; j++) {
			if (percentPullWep4Star(j, num4Star, pity, simAmt, want5050, guarantee) > percentage) {
				pull = j;
				break;
			}
		}
		
		return pull;
	}
	
	
	public static double percentPull4Star(int numPulls, int num4Star, int pity, int simAmt, boolean want5050, boolean guarantee) {
		ArrayList<Integer> fourStar = new ArrayList<>();
		while (simAmt > 0) {
			//System.out.println("sim#: " + simAmt);
			fourStar.add(sim4Star(numPulls, num4Star, pity, want5050, guarantee).get(0));
			simAmt--;
		}
		
		double count = 0;
		for (int i : fourStar) {
			if (i >= num4Star) {
				count++;
			}
		}
		return 100 * count / fourStar.size();
	}
	
	public static double percentPullWep4Star(int numPulls, int num4Star, int pity, int simAmt, boolean want5050, boolean guarantee) {
		ArrayList<Integer> fourStar = new ArrayList<>();
		while (simAmt > 0) {
			//System.out.println("sim#: " + simAmt);
			fourStar.add(simWep4Star(numPulls, num4Star, pity, want5050, guarantee).get(0));
			simAmt--;
		}
		
		double count = 0;
		for (int i : fourStar) {
			if (i >= num4Star) {
				count++;
			}
		}
		return 100 * count / fourStar.size();
	}
	
	
	public static ArrayList<Integer> sim4Star(int numPulls, int numWant4Star, int pity, boolean want5050, boolean guarantee) {
		ArrayList<Integer> charInfo = new ArrayList<>();
		ArrayList<Integer> pullsChar = new ArrayList<>();
		int fourStar = 0;
		int numGood4Star = 0;
		int pullsTook = 0;
		int num1;
		int initPity = pity;
		boolean fourStarGood = true;
		
		if (initPity + numPulls > 10) {
			num1 = 10;
		} else num1 = initPity + numPulls;

		while (numPulls > 0) {
			
			//stops pulling once reached target amt of 4*s
			if ((numGood4Star >= numWant4Star) && (fourStar >= numWant4Star)) break;
			
			double percent = 51;
			
			for (int j = initPity + 1; j <= num1; j++) {

				if (j == 9) {
					percent = 525;
				} else if (j == 10) {
					percent = 1000;
				}

				
				double rand2 = Math.random() * 3;
				
				if (Math.random() * 1000 < percent) {
					pullsChar.add(j);
					//System.out.println(j);
					if (want5050) {
					
//						5050 code
						if (guarantee) {
							if (rand2 < 1) {
							//got wanted 4*
								fourStarGood = true;
							} else {
								fourStarGood = false;
							}
							guarantee = false;
						} else if (fourStarGood) {
							if (Math.random() * 2 < 1) {
								//missed 5050
								fourStarGood = false;
							} else if (rand2 < 1) {
								fourStarGood = true;
							} else {
								fourStarGood = false;
							}
						} else {
							if (rand2 < 1) {
								fourStarGood = true;
							}
						}
					
						if (fourStarGood) {
							numGood4Star++;
						}
//						5050 code end
					}
					
					fourStar++;
					pullsTook = j - initPity;
					initPity = 0;
					break;
				}
			}
				
			numPulls -= pullsTook;
			if (numPulls > 10) {
				num1 = 10;
			} else {
				num1 = numPulls;
				numPulls = 0;
			}
			
		}
		
		
		if (want5050) {
			charInfo.add(numGood4Star);
			charInfo.add(numPulls);
			return charInfo;
		} else {
			charInfo.add(fourStar);
			charInfo.add(numPulls);
			return charInfo;
		}
	}
	
	public static ArrayList<Integer> simWep4Star(int numPulls, int numWant4Star, int pity, boolean want5050, boolean guarantee) {
		ArrayList<Integer> wepInfo = new ArrayList<>();
		ArrayList<Integer> pullsWep = new ArrayList<>();
		int fourStar = 0;
		int numGood4Star = 0;
		int pullsTook = 0;
		int num1;
		int initPity = pity;
		boolean fourStarGood = true;
		
		if (initPity + numPulls > 10) {
			num1 = 10;
		} else num1 = initPity + numPulls;

		while (numPulls > 0) {
			
			//stops pulling once reached target amt of 4*s
			if ((numGood4Star >= numWant4Star) && (fourStar >= numWant4Star)) break;
			
			double percent = 60;
			
			for (int j = initPity + 1; j <= num1; j++) {

				if (j == 9) {
					percent = 53;
				} else if (j == 10) {
					percent = 1000;
				}

				
				double rand2 = Math.random() * 5;
				
				if (Math.random() * 1000 < percent) {
					pullsWep.add(j);
					//System.out.println(j);
					if (want5050) {
					
//						5050 code
						if (guarantee) {
							if (rand2 < 1) {
							//got wanted 4*
								fourStarGood = true;
							} else {
								fourStarGood = false;
							}
							guarantee = false;
						} else if (fourStarGood) {
							if (Math.random() * 4 < 3) {
								//missed 5050
								fourStarGood = false;
							} else if (rand2 < 1) {
								fourStarGood = true;
							} else {
								fourStarGood = false;
							}
						} else {
							if (rand2 < 1) {
								fourStarGood = true;
							}
						}
					
						if (fourStarGood) {
							numGood4Star++;
						}
//						5050 code end
					}
					
					fourStar++;
					pullsTook = j - initPity;
					initPity = 0;
					break;
				}
			}
				
			numPulls -= pullsTook;
			if (numPulls > 10) {
				num1 = 10;
			} else {
				num1 = numPulls;
				numPulls = 0;
			}
			
		}
		
		
		if (want5050) {
			wepInfo.add(numGood4Star);
			wepInfo.add(numPulls);
			return wepInfo;
		} else {
			wepInfo.add(fourStar);
			wepInfo.add(numPulls);
			return wepInfo;
		}
	}
	
	
	public static ArrayList<Integer> simChar(int numPulls, int numWant5Star, int pity, boolean want5050, boolean guarantee) {
		ArrayList<Integer> charInfo = new ArrayList<>();
		ArrayList<Integer> pullsChar = new ArrayList<>();
		int fiveStar = 0;
		int numGood5Star = 0;
		int pullsTook = 0;
		int num1;
		int initPity = pity;
		boolean fiveStarGood = true;
		
		if (initPity + numPulls > 90) {
			num1 = 90;
		} else num1 = initPity + numPulls;

		while (numPulls != 0) {
			
			//stops pulling once reached target amt of 5*s
			if ((numGood5Star >= numWant5Star) && (fiveStar >= numWant5Star)) break;
			
			double percent = 6;
			
			for (int j = initPity; j <= num1; j++) {

				if (j == 74) {
					percent = 69.4;
				} else if (j == 75) {
					percent = 128.3;
				} else if (j == 76) {
					percent = 189.6;
				} else if (j == 77) {
					percent = 247.7;
				} else if (j == 78) {
					percent = 306.9;
				} else if (j == 79) {
					percent = 363.6;
				} else if (j == 80) {
					percent = 427.9;
				} else if (j == 81) {
					percent = 483.9;
				} else if (j == 82) {
					percent = 543.6;
				} else if (j == 83) {
					percent = 596.1;
				} else if (j == 84) {
					percent = 657.1;
				} else if (j == 85) {
					percent = 725;
				} else if (j == 86) {
					percent = 787.9;
				} else if (j == 87) {
					percent = 843.3;
				} else if (j == 88) {
					percent = 902.7;
				} else if (j == 89) {
					percent = 962.1;
				} else if (j == 90) {
					percent = 1000;
				}
				
				if (Math.random() * 1000 < percent) {
					pullsChar.add(j);
//					if (j >= 74) {
//					System.out.println("pity " + j);
//					} else {
//						System.out.println(j);
//					}
					
					if (want5050) {
					
//						5050 code
						if (guarantee) {
							//next 5* is guarantee
							fiveStarGood = true;
							guarantee = false;
						} else if (fiveStarGood) {
							if (Math.random() * 2 < 1) {
								//missed 5050
								fiveStarGood = false;
							} else fiveStarGood = true;
						} else fiveStarGood = true;
					
						if (fiveStarGood) {
							numGood5Star++;
						}
//						5050 code end
					}
					
					fiveStar++;
					pullsTook = j - initPity + 1;
					initPity = 1;
					break;
				}
			}
			if (numPulls > 90) {
				numPulls -= pullsTook;
				if (numPulls < 90) num1 = numPulls;
			} else {
				num1 = numPulls;
				numPulls = 0;
			}
			
		}
		
		if (want5050) {
			charInfo.add(numGood5Star);
			charInfo.add(numPulls);
			return charInfo;
		} else {
			charInfo.add(fiveStar);
			charInfo.add(numPulls);
			return charInfo;
		}
	}
	
	public static double percentPullChar(int numPulls, int num5Star, int pity, int simAmt, boolean want5050, boolean guarantee) {
		ArrayList<Integer> fiveStar = new ArrayList<>();
		while (simAmt > 0) {
			fiveStar.add(simChar(numPulls, num5Star, pity, want5050, guarantee).get(0));
			simAmt--;
		}
		
		double count = 0;
		for (int i : fiveStar) {
			if (i >= num5Star) {
				count++;
			}
		}
		return 100 * count / fiveStar.size();
	}
	
	public static ArrayList<Integer> simWep(int numPulls, int numWant5Star, int pity, boolean wantWepScam, int epitomePity) {
		ArrayList<Integer> wepInfo = new ArrayList<>();
		ArrayList<Integer> pullsWep = new ArrayList<>();
		int fiveStar = 0;
		int numGood5Star = 0;
		int numBannerBad5Star = 0;
		int numOff5Star = 0;
		int pullsTook = 0;
		int num1;
		int initPity = pity;
		boolean guarantee = false;
		int eCount = epitomePity;
		
		if (initPity + numPulls > 80) {
			num1 = 80;
		} else num1 = initPity + numPulls;
		
		while (numPulls > 0) {
			
			//stops pulling once reached target amt of 5*s
			if ((numGood5Star >= numWant5Star) && (fiveStar >= numWant5Star)) break;
			
			double percent = 7;
			
			for (int j = initPity; j <= num1; j++) {

				if (j == 63) {
					percent = 86.5;
				} else if (j == 64) {
					percent = 155.4;
				} else if (j == 65) {
					percent = 222;
				} else if (j == 66) {
					percent = 283.9;
				} else if (j == 67) {
					percent = 361.4;
				} else if (j == 68) {
					percent = 424.9;
				} else if (j == 69) {
					percent = 495.1;
				} else if (j == 70) {
					percent = 558.5;
				} else if (j == 71) {
					percent = 626.2;
				} else if (j == 72) {
					percent = 693.9;
				} else if (j == 73) {
					percent = 761.6;
				} else if (j == 74) {
					percent = 829.3;
				} else if (j == 75) {
					percent = 897;
				} else if (j == 76) {
					percent = 964.7;
				} else if (j == 77) {
					percent = 990.35;
				} else if (j == 78) {
					percent = 992.22;
				} else if (j == 79) {
					percent = 993.48;
				} else if (j == 80) {
					percent = 1000;
				}
				
				if (Math.random() * 1000 < percent) {
					pullsWep.add(j);
//					System.out.println(j);

					if (wantWepScam) {
					
//						wep-scam code
						
						if (eCount == 2) {
							guarantee = true;
							
							//reset values
							eCount = 0;
						}
						
						double rand = Math.random() * 10;
//						System.out.println("rand " + rand);
						if ((rand < 3.75) || guarantee) {
							//got wanted
							numGood5Star++;
							
							//reset values
							if (guarantee) {
								guarantee = false;
							}

						} else if (rand < 7.5) { //7.5 cuz if rand !< 3.75, another 3.75 chance is < 7.5
							//got unwanted on banner
							numBannerBad5Star++;
							eCount++;
						} else {
							//got off banner
							numOff5Star++;
							eCount++;
						}
						
//						wepscam code end
					}
//					System.out.println("numgood5star " + numGood5Star);
//					System.out.println("epity " + eCount);

					
					fiveStar++;
					pullsTook = j - initPity;
					initPity = 0;
					break;
				}
			}
			numPulls -= pullsTook;
			if (numPulls > 80) {
				num1 = 80;
			} else {
				num1 = numPulls;
				numPulls = 0;
			}
			
		}
		if (wantWepScam) {
			wepInfo.add(numGood5Star);
//			System.out.println("numgood5star " + numGood5Star);
			wepInfo.add(numBannerBad5Star);
//			System.out.println("numbad5star " + numBannerBad5Star);
			wepInfo.add(numOff5Star);
//			System.out.println("numoff5star " + numOff5Star);
			wepInfo.add(numPulls);
			return wepInfo;
		} else {
			wepInfo.add(fiveStar);
			wepInfo.add(numPulls);
			return wepInfo;
		}
	}
	
	public static ArrayList<Double> percentPullWep(int numPulls, int num5Star, int pity, int simAmt, boolean want5050, int ePity) {
		ArrayList<Double> wepPercentInfo = new ArrayList<>();
		ArrayList<Integer> fiveStarGood = new ArrayList<>();
		ArrayList<Integer> fiveStarBannerBad = new ArrayList<>();
		ArrayList<Integer> fiveStarOffBanner = new ArrayList<>();

		while (simAmt > 0) {
			ArrayList<Integer> wepsim = simWep(numPulls, num5Star, pity, want5050, ePity);
			fiveStarGood.add(wepsim.get(0));
			fiveStarBannerBad.add(wepsim.get(1));
			fiveStarOffBanner.add(wepsim.get(2));

			simAmt--;
		}
		
		double count1 = 0;
		for (int i : fiveStarGood) {
			if (i >= num5Star) {
				count1++;
			}
		}
		
		double count2 = 0;
		for (int i : fiveStarBannerBad) {
			count2 += i;
		}
		
		double count3 = 0;
		for (int i : fiveStarOffBanner) {
			count3 += i;
		}
		
		wepPercentInfo.add(100 * count1 / fiveStarGood.size());
		wepPercentInfo.add(count2 / fiveStarBannerBad.size());
		wepPercentInfo.add(count3 / fiveStarBannerBad.size());
		
		return wepPercentInfo;

	}
	
}
