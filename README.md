# day5-jdbc 코딩

### 프로젝트 구조

![프로젝트 구성.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/7c6273d5-c60e-4cc6-8cd5-74c64a5dc62d/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8_%EA%B5%AC%EC%84%B1.png)

1. 프로젝트 구성

| 프로젝트 구조 | 설명 |
| --- | --- |
| src/main/java | 자바의 클래스 파일을 저장하는 공간 |
| JRE System Library | 자바의 표준 라이브러리 |
| Maven Dependencies | pom.xml에 설정한 라이브러리를 확인할 수 있는 공간 |
| src 폴더 | img, html, css, javascript 파일을 저장하는 공간 |
| pom.xml | 라이브러리 다운을 받기 위한 설정 공간 |
- Maven은 라이브러리를 관리하고 프로젝트를 빌드하는 도구입니다.
- 사용하는 라이브러리가 많아질수록 관리의 필요성이 생기는데, Maven 프로젝트에서는 특정 문서(pom.xml)에 사용할 라이브러리 정의하면 네트워크를 통해 자동으로 다운 받아 줍니다.

1. 자바 클래스 구성

| 클래스 파일 | 설명 |
| --- | --- |
| PeopleDao.java | DB의 data에 접근하기 위한 객체와 DB와 연결할 Connection으로 구성 |
| PeopleVo.java | 계층 간에 데이터 교환을 위한 객체 |
| ReadBuffer.java | csv file의 data 접근과 insert / select 메소드 호출  |

---

### JDBC (ojdbc8.jar 버전) 사용

![jdbc설명.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/26e894e7-d965-4f2a-b630-5a4303b16c20/jdbc%EC%84%A4%EB%AA%85.png)

1. JDBC란?
- Java Database Connectivity의 약자로 자바를 이용하여 데이터베이스에 접근하여 각종 SQL문을 수행할 수 있도록 하는 JAVA API 입니다.
- JDBC는 크게 JDBC 인터페이스와 JDBC 드라이버로 구성되어 있습니다.
    
    
    | 종류 | 기능 |
    | --- | --- |
    | JDBC 인터페이스 | java.sql.Connection(연결) / java.sql.Statement(SQL을 담을 내용) / java.sql.ResultSet(SQL 요청 응답) |
    | JDBC 드라이버 | DB와의 통신을 담당하는 인터페이스 |

1. JDBC의 동작 흐름 
- 응용 프로그램에서는 SQL문을 만들어 JDBC 인터페이스를 통해 전송하면 실제 구현 클래스인 JDBC 드라이버에서 DBMS에 접속을 시도하여 SQL문을 전송하게 됩니다.
- DBMS의 결과는 JDBC 드라이버와 JDBC 인터페이스에 전달되고, 이를 다시 응용 프로그램으로 전달되어 SQL문의 결과를 볼 수 있습니다.
- 이처럼 JDBC는 Application과 DBMS의 중간 다리 역할을 수행합니다.

---

### 코드 및 수행 결과

1. 코드
- PeopleDao.java

```java
package com.home.maven;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PeopleDao {
	
	//싱글톤
	private static PeopleDao instance;
	private PeopleDao() {}
	public static PeopleDao getInstance() {
		if(instance == null) {
			instance = new PeopleDao();
		}
		return instance;
	}
	
	//jdbc
	private final String DRIVER_NAME = "oracle.jdbc.driver.OracleDriver";
	private final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private final String ID = "yejin";
	private final String PW = "1234";
	
	//getConnection
	private Connection getConnection() {		
			try {
				// 오라클 드라이버를 메모리에 로딩
				Class.forName(DRIVER_NAME);
				Connection conn = DriverManager.getConnection(
						URL, ID, PW);
				//System.out.println("접속성공");
				return conn;
			} catch (Exception e) {
				e.printStackTrace(); 
			}
			return null;
		} 
		
		private void closeAll(Connection conn, PreparedStatement pstmt, ResultSet rs) {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		//insert
		public boolean insert(PeopleVo peopleVo) {
			Connection conn = null; //접속
			PreparedStatement pstmt = null; //SQL 문장
			
			try {
				conn = getConnection();
				String sql = "insert into tbl_people(no,name,age,job)"
						+ "   	values(no.nextval,?, ?, ?)";
				
				pstmt = conn.prepareStatement(sql);
				
				
				pstmt.setString(1, peopleVo.getName());
				pstmt.setString(2, peopleVo.getAge());
				pstmt.setString(3, peopleVo.getJob());
				
				int count = pstmt.executeUpdate();
				
				if (count > 0) {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				closeAll(conn, pstmt, null);
			}
			
			return false;
		}
		
		//select
		public List<PeopleVo> selectAll() {
			Connection conn = null; // 접속
			PreparedStatement pstmt = null; // SQL 문장
			ResultSet rs = null; // 결과 저장
			
			try {
				conn = getConnection();
				String sql = "select * from tbl_people";
				
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				
				List<PeopleVo> list = new ArrayList<>();
				
				while (rs.next()) {
					
					String name = rs.getString("name");
					String age = rs.getString("age");
					String job = rs.getString("job");
					
					PeopleVo vo = new PeopleVo(name,age,job);				
					
					list.add(vo);
				}
				return list;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				closeAll(conn, pstmt, rs);
			}
			return null;
		}

		
		
		
}
```

- PeopleVo.java

```java
package com.home.maven;

public class PeopleVo {
	private String name;
	private String age;
	private String job;
	
	
	public PeopleVo() {
		super();
	}

	public PeopleVo(String name, String age, String job) {
		super();
		this.name = name;
		this.age = age;
		this.job = job;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	@Override
	public String toString() {
		return "peopleVo [name=" + name + ", age=" + age + ", job=" + job + "]";
	}
	
	
}
```

- ReadBuffer.java

```java
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
						boolean resultInsert = dao.insert(peopleVo);
						
						//데이터 정상 입력되는지 확인하기
						if (resultInsert == true) {
							System.out.println("데이터 입력 성공");
						} else {
							System.out.println("데이터 입력 실패");
						}	
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
```

- DBMS(Oracle) Table

```java
// 시퀀스 생성
create SEQUENCE no
INCREMENT by 1
START WITH 1;

// 테이블 생성
create table tbl_people(
    no number PRIMARY KEY,
    name varchar(20) not null,
    age varchar(10) not null,
    job varchar2(20) not null
);
```

1. 수행 결과
- csv file

![csv 파일.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/ac53f055-1381-4c65-9ed5-fea15e0cc6a8/csv_%ED%8C%8C%EC%9D%BC.png)

- insert 결과

![table insert.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/06c6ff1d-d14e-4730-8657-f02ab793d348/table_insert.png)

- select 결과
![selectAll.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/cf2fa2cb-eb4d-472e-afae-8bb724ceb9d9/selectAll.png)
