package com.home.maven;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class ReadBuffer {

	public static void main(String[] args) throws IOException {
		
				//BufferedReader 대용량 파일을 읽어오기 적합 + csv 파일의 경로
				BufferedReader file = new BufferedReader(new FileReader("C:\\maven\\example.csv"));
				String line = "";
				
				//파일을 한줄씩 읽어오기
				try {
					
					//PeopleDao 객체
					PeopleDao dao = PeopleDao.getInstance();
					
					while((line = file.readLine())!=null) {
						//System.out.println(line);
						
						//파일의 한 줄을 ,로 구분해 배열에 저장 후 리스트로 반환하기
						String[] arr = line.split(",");
									
						PeopleVo peopleVo = new PeopleVo();
												
						peopleVo.setName(arr[0]); //첫번째 칼럼 name의 데이터
						//System.out.println(arr[0]); 
						
						peopleVo.setAge(arr[1]); //두번째 칼럼 age의 데이터
						//System.out.println(arr[1]); 
						
						peopleVo.setJob(arr[2]); //세번째 칼럼 job의 데이터
						//System.out.println(arr[2]); 
						
						
						//insert 
//						boolean resultInsert = dao.insert(peopleVo);
//						
//						//데이터 정상 입력되는지 확인하기
//						if (resultInsert == true) {
//							System.out.println("데이터 입력 성공");
//						} else {
//							System.out.println("데이터 입력 실패");
//						}		
					}//while문
					
					//selectAll
					List<PeopleVo> list = dao.selectAll();
					//System.out.println(list);
					
					System.out.println("이름\t나이\t직업");
					System.out.println("==========================");
					
					for(PeopleVo vo : list ) {
						String name = vo.getName();
						String age = vo.getAge();
						String job = vo.getJob();
						
						System.out.println(name + "\t" + age + " \t" + job );
						
					
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}			

	}

}
