package com.sku.TourList.service;

import com.sku.TourList.domain.Tour;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ApiServiceimpl implements ApiService{
    // 지역 조회 코드, 지역 기반, 공통정보
    // tag값의 정보를 가져오는 메소드
    private static String getTagValue(String tag, Element eElement) {
        if(eElement.getElementsByTagName(tag).item(0) == null)
            return "없음";
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes(); // null

        Node nValue = (Node) nlList.item(0);
        if(nValue == null)
            return "없음";
        return nValue.getNodeValue();
    }

    @Override
    public List<Tour> CallRegion(String number) {
        int page = 1;	// 페이지 초기값
        List<Tour> result = new ArrayList<> ();

        try {
            StringBuilder url = new StringBuilder ("http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaCode");
            url.append ("?" + URLEncoder.encode ("ServiceKey", "UTF-8") + "=AYVQTVFCUQ0Wda6v9brkZWrDRd2GBdfajmkkf0CLCdJVAuU2N0gz2S%2BmQvPNBlItSekbZy4ek%2BI3n7JZ3AIVYA%3D%3D");
            url.append ("&" + URLEncoder.encode ("pageNo", "UTF-8") + "=" + URLEncoder.encode ("1", "UTF-8"));
            url.append ("&" + URLEncoder.encode ("numOfRows", "UTF-8") + "=" + URLEncoder.encode ("10000", "UTF-8"));
            url.append ("&" + URLEncoder.encode ("MobileApp", "UTF-8") + "=AppTest");
            url.append ("&" + URLEncoder.encode ("MobileOS", "UTF-8") + "=ETC");
            url.append ("&" + URLEncoder.encode ("areaCode", "UTF-8") + "=" + URLEncoder.encode (number, "UTF-8"));

            while(true){
                DocumentBuilderFactory DBFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = DBFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(url.toString ()); // url 문서를 가지고옴

                // root tag
                //doc.getDocumentElement().normalize();
                //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

                // 파싱할 tag
                NodeList nList = doc.getElementsByTagName("item");
                //System.out.println("파싱할 리스트 수 : "+ nList.getLength());  // 파싱할 리스트 수 = 총 item 수

                for(int temp = 0; temp < nList.getLength(); temp++){
                    Node nNode = nList.item(temp);
                    if(nNode.getNodeType() == Node.ELEMENT_NODE){
                        Tour tour = new Tour();
                        Element eElement = (Element) nNode;
                        //System.out.println(eElement.getTextContent());

                        tour.setAddr1 (getTagValue ("addr1", eElement));
                        tour.setAddr2 (getTagValue ("addr2", eElement));
                        tour.setAreacode (getTagValue ("areacode", eElement));
                        tour.setCat1 (getTagValue ("cat1", eElement));
                        tour.setCat2 (getTagValue ("cat2", eElement));
                        tour.setCat3 (getTagValue ("cat3", eElement));
                        tour.setContentid (getTagValue ("contentid", eElement));
                        tour.setContenttypeid (getTagValue ("contenttypeid", eElement));
                        tour.setCreatedtime (getTagValue ("createdtime", eElement));
                        tour.setFirstimage (getTagValue ("firstimage", eElement));
                        tour.setFirstimage2 (getTagValue ("firstimage2", eElement));
                        tour.setMapx (Double.parseDouble (getTagValue ("mapx", eElement)));
                        tour.setMapy (Double.parseDouble (getTagValue ("mapy", eElement)));
                        tour.setMlevel (getTagValue ("mlevel", eElement));
                        tour.setModifiedtime (getTagValue ("modifiedtime", eElement));
                        tour.setReadcount (getTagValue ("readcount", eElement));
                        tour.setSigungucode (getTagValue ("sigungucode", eElement));
                        tour.setTitle (getTagValue ("title", eElement));
                        tour.setZipcode (getTagValue ("zipcode", eElement));

                        result.add (tour);
                    }	// if end
                }	// for end

                page += 1;
                System.out.println("page number : "+page);
                if(page > 1){
                    break;
                }
            }	// while end

        } catch (Exception e) {
            e.printStackTrace ();
        }
        return result;
    }

    @Override
    public List<Tour> CallApi(String areaCode, String sigunguCode) {
        int page = 1;	// 페이지 초기값
        List<Tour> result = new ArrayList<> ();
        // 오늘 날짜입력
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        //Calendar c1 = Calendar.getInstance();
        //String strToday = sdf.format(c1.getTime());
        //System.out.println("Today=" + strToday);

        try {
            StringBuilder url = new StringBuilder ("http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList");
            url.append ("?" + URLEncoder.encode ("ServiceKey", "UTF-8") + "=AYVQTVFCUQ0Wda6v9brkZWrDRd2GBdfajmkkf0CLCdJVAuU2N0gz2S%2BmQvPNBlItSekbZy4ek%2BI3n7JZ3AIVYA%3D%3D");
            url.append ("&" + URLEncoder.encode ("pageNo", "UTF-8") + "=" + URLEncoder.encode ("1", "UTF-8"));
            url.append ("&" + URLEncoder.encode ("numOfRows", "UTF-8") + "=" + URLEncoder.encode ("10000", "UTF-8"));
            url.append ("&" + URLEncoder.encode ("MobileApp", "UTF-8") + "=AppTest");
            url.append ("&" + URLEncoder.encode ("MobileOS", "UTF-8") + "=ETC");
            url.append ("&" + URLEncoder.encode ("arrange", "UTF-8") + "=A");
            url.append ("&" + URLEncoder.encode ("contentTypeId", "UTF-8")); // 12여행지 / 32음식점 / 39호텔
            url.append ("&" + URLEncoder.encode ("areaCode", "UTF-8") + "=" + URLEncoder.encode (areaCode, "UTF-8"));
            url.append ("&" + URLEncoder.encode ("sigunguCode", "UTF-8") + "=" + URLEncoder.encode (sigunguCode, "UTF-8"));
            url.append ("&" + URLEncoder.encode ("listYN", "UTF-8") + "=Y");

            while(true){
                DocumentBuilderFactory DBFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = DBFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(url.toString ()); // url 문서를 가지고옴

                // root tag
                //doc.getDocumentElement().normalize();
                //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

                // 파싱할 tag
                NodeList nList = doc.getElementsByTagName("item");
                //System.out.println("파싱할 리스트 수 : "+ nList.getLength());  // 파싱할 리스트 수 = 총 item 수

                for(int temp = 0; temp < nList.getLength(); temp++){
                    Node nNode = nList.item(temp);
                    if(nNode.getNodeType() == Node.ELEMENT_NODE){
                        Tour tour = new Tour();
                        Element eElement = (Element) nNode;
                        //System.out.println(eElement.getTextContent());

                        tour.setAddr1 (getTagValue ("addr1", eElement));
                        tour.setAddr2 (getTagValue ("addr2", eElement));
                        tour.setAreacode (getTagValue ("areacode", eElement));
                        tour.setCat1 (getTagValue ("cat1", eElement));
                        tour.setCat2 (getTagValue ("cat2", eElement));
                        tour.setCat3 (getTagValue ("cat3", eElement));
                        tour.setContentid (getTagValue ("contentid", eElement));
                        tour.setContenttypeid (getTagValue ("contenttypeid", eElement));
                        tour.setCreatedtime (getTagValue ("createdtime", eElement));
                        tour.setFirstimage (getTagValue ("firstimage", eElement));
                        tour.setFirstimage2 (getTagValue ("firstimage2", eElement));
                        tour.setMapx (Double.parseDouble (getTagValue ("mapx", eElement)));
                        tour.setMapy (Double.parseDouble (getTagValue ("mapy", eElement)));
                        tour.setMlevel (getTagValue ("mlevel", eElement));
                        tour.setModifiedtime (getTagValue ("modifiedtime", eElement));
                        tour.setReadcount (getTagValue ("readcount", eElement));
                        tour.setSigungucode (getTagValue ("sigungucode", eElement));
                        tour.setTitle (getTagValue ("title", eElement));
                        tour.setZipcode (getTagValue ("zipcode", eElement));

                        result.add (tour);
                    }	// if end
                }	// for end

                page += 1;
                if(page > 1){
                    break;
                }
            }	// while end

        } catch (Exception e) {
            e.printStackTrace ();
        }
        return result;
    }

    @Override
    public Tour CallDetail(String contentId){
        int page = 1;	// 페이지 초기값
        Tour result = new Tour ();

        try {
            StringBuilder url = new StringBuilder ("http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon");
            url.append ("?" + URLEncoder.encode ("ServiceKey", "UTF-8") + "=AYVQTVFCUQ0Wda6v9brkZWrDRd2GBdfajmkkf0CLCdJVAuU2N0gz2S%2BmQvPNBlItSekbZy4ek%2BI3n7JZ3AIVYA%3D%3D");
            url.append ("&" + URLEncoder.encode ("numOfRows", "UTF-8") + "=" + URLEncoder.encode ("10000", "UTF-8"));
            url.append ("&" + URLEncoder.encode ("pageNo", "UTF-8") + "=" + URLEncoder.encode ("1", "UTF-8"));
            url.append ("&" + URLEncoder.encode ("MobileOS", "UTF-8") + "=ETC");
            url.append ("&" + URLEncoder.encode ("MobileApp", "UTF-8") + "=AppTest");
            url.append ("&" + URLEncoder.encode ("contentId", "UTF-8") + "=" + URLEncoder.encode (contentId, "UTF-8"));
            url.append ("&" + URLEncoder.encode ("contentTypeId", "UTF-8")); // 12여행지 / 32음식점 / 39호텔
            url.append ("&" + URLEncoder.encode ("defaultYN", "UTF-8") + "=Y");
            url.append ("&" + URLEncoder.encode ("firstImageYN", "UTF-8") + "=Y");
            url.append ("&" + URLEncoder.encode ("areacodeYN", "UTF-8") + "=Y");
            url.append ("&" + URLEncoder.encode ("catcodeYN", "UTF-8") + "=Y");
            url.append ("&" + URLEncoder.encode ("addrinfoYN", "UTF-8") + "=Y");
            url.append ("&" + URLEncoder.encode ("mapinfoYN", "UTF-8") + "=Y");
            url.append ("&" + URLEncoder.encode ("overviewYN", "UTF-8") + "=Y");

            while(true){
                DocumentBuilderFactory DBFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = DBFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(url.toString ()); // url 문서를 가지고옴

                // root tag
                //doc.getDocumentElement().normalize();
                //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

                // 파싱할 tag
                NodeList nList = doc.getElementsByTagName("item");
                //System.out.println("파싱할 리스트 수 : "+ nList.getLength());  // 파싱할 리스트 수 = 총 item 수

                for(int temp = 0; temp < nList.getLength(); temp++){
                    Node nNode = nList.item(temp);
                    if(nNode.getNodeType() == Node.ELEMENT_NODE){
                        Element eElement = (Element) nNode;
                        //System.out.println(eElement.getTextContent());

                        result.setAddr1 (getTagValue ("addr1", eElement));
                        result.setAddr2 (getTagValue ("areacode", eElement));
                        result.setBooktour (getTagValue ("booktour", eElement));
                        result.setCat1 (getTagValue ("cat1", eElement));
                        result.setCat2 (getTagValue ("cat2", eElement));
                        result.setCat3 (getTagValue ("cat3", eElement));
                        result.setContentid (getTagValue ("contentid", eElement));
                        result.setContenttypeid (getTagValue ("contenttypeid", eElement));
                        result.setCreatedtime (getTagValue ("createdtime", eElement));
                        result.setFirstimage (getTagValue ("firstimage", eElement));
                        result.setFirstimage2 (getTagValue ("firstimage2", eElement));
                        result.setHomepage (getTagValue ("homepage", eElement));
                        result.setMapx (Double.parseDouble (getTagValue ("mapx", eElement)));
                        result.setMapy (Double.parseDouble (getTagValue ("mapy", eElement)));
                        result.setMlevel (getTagValue ("mlevel", eElement));
                        result.setModifiedtime (getTagValue ("modifiedtime", eElement));
                        result.setOverview (getTagValue ("overview", eElement));
                        result.setSigungucode (getTagValue ("sigungucode", eElement));
                        result.setTitle (getTagValue ("title", eElement));
                        result.setZipcode (getTagValue ("zipcode", eElement));

                    }	// if end
                }	// for end

                page += 1;
                if(page > 1){
                    break;
                }
            }	// while end

        } catch (Exception e) {
            e.printStackTrace ();
        }
        return result;
    }
}
/*
<addr1>대구광역시 동구 서촌로23길 13-11</addr1>
<addr2>(덕곡동)</addr2>
<areacode>4</areacode>
<cat1>A05</cat1>
<cat2>A0502</cat2>
<cat3>A05020100</cat3>
<contentid>2745657</contentid>
<contenttypeid>39</contenttypeid>
<createdtime>20210915153749</createdtime>
<firstimage>http://tong.visitkorea.or.kr/cms/resource/90/2746190_image2_1.jpg</firstimage>
<firstimage2>http://tong.visitkorea.or.kr/cms/resource/90/2746190_image2_1.jpg</firstimage2>
<mapx>128.6205183884</mapx>
<mapy>35.9841102996</mapy>
<mlevel>6</mlevel>
<modifiedtime>20210916140557</modifiedtime>
<readcount>0</readcount>
<sigungucode>4</sigungucode>
<title>가마솥에 누른 밥</title>
<zipcode>41000</zipcode>
*/

/*
<addr1>서울특별시 종로구 사직로 161</addr1>
<areacode>1</areacode>
<booktour>1</booktour>
<cat1>A02</cat1>
<cat2>A0201</cat2>
<cat3>A02010100</cat3>
<contentid>126508</contentid>
<contenttypeid>12</contenttypeid>
<createdtime>20031230090000</createdtime>
<firstimage>http://tong.visitkorea.or.kr/cms/resource/23/2678623_image2_1.jpg</firstimage>
<firstimage2>http://tong.visitkorea.or.kr/cms/resource/23/2678623_image3_1.jpg</firstimage2>
<homepage>경복궁 <a href="http://www.royalpalace.go.kr/" target="_blank" title="새창 : 경복궁 홈페이지로 이동">http://www.royalpalace.go.kr</a><br />궁궐길라잡이 <a href="http://www.palaceguide.or.kr/" target="_blank" title="새창 : 궁궐길라잡이 홈페이지로 이동">http://www.palaceguide.or.kr</a><br />한국의재발견 <a href="http://www.rekor.or.kr/" target="_blank" title="새창 : 한국의재발견 홈페이지로 이동">http://www.rekor.or.kr</a><br />야간관람 예매<a href="https://ticket.11st.co.kr/Product/Detail?id=266194&prdNo=4239172482"title="새창: 경복궁 야간관람 예매"target="_blank">https://ticket.11st.co.kr/</a></homepage>
<mapx>126.9769930325</mapx>
<mapy>37.5788222356</mapy>
<mlevel>6</mlevel>
<modifiedtime>20220708105358</modifiedtime>
<overview>경복궁은 1395년 태조 이성계에 의해서 새로운 조선왕조의 법궁으로 지어졌다. 경복궁은 동궐(창덕궁)이나 서궐(경희궁)에 비해 위치가 북쪽에 있어 '북궐'이라 불리기도 했다. 경복궁은 5대 궁궐 가운데 으뜸의 규모와 건축미를 자랑한다. 경복궁 근정전에서 즉위식을 가진 왕들을 보면 제2대 정종, 제4대 세종, 제6대 단종, 제7대 세조, 제9대 성종, 제11대 중종, 제13대 명종 등이다. 경복궁은 임진왜란 때 상당수의 건물이 불타 없어진 아픔을 갖고 있으며, 고종 때에 흥선대원군의 주도 아래 7,700여칸에 이르는 건물들을 다시 세웠다. 그러나 또 다시 명성황후 시해사건이 일어나면서 왕조의 몰락과 함께 경복궁도 왕궁으로서의 기능을 상실하고 말았다. 경복궁에는 조선시대의 대표적인 건축물인 경회루와 향원정의 연못이 원형대로 남아 있으며, 근정전의 월대와 조각상들은 당시의 조각미술을 대표한다. 현재 흥례문 밖 서편에는 국립고궁 박물관이 위치하고 있고, 경복궁 내 향원정의 동편에는 국립민속 박물관이 위치하고 있다.<br /><br />* 주요문화재 <br />1) 사적 경복궁<br>2) 국보 경복궁 근정전<br>3) 국보 경복궁 경회루<br>4) 보물 경복궁 자경전<br>5) 보물 경복궁 자경전 십장생 굴뚝<br>6) 보물 경복궁 아미산굴뚝<br>7) 보물 경복궁 근정문 및 행각<br>8) 보물 경복궁 풍기대<br></overview>
<sigungucode>23</sigungucode>
<title>경복궁</title>
<zipcode>03045</zipcode>
*/
