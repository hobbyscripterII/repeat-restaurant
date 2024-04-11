var mapOptions = { center: new naver.maps.LatLng(37.3595704, 127.105399), zoom: 10, zoomControl: true }; // 지도 로드 시 초기 값 세팅
var map = new naver.maps.Map('map', mapOptions); // 해당 좌표 축으로 지도 표시

document.addEventListener('click', (e) => {
    if(e.target.id == 'btn-coordinate') {
        const addr = document.getElementById('input-addr').value;

        if(!addr) { alert('업체명을 입력해주세요.'); } // search 사용 시에는 업체명으로 변경
        else {
            $.ajax({
                type: 'get', url: '/naver/search', data: {'addr': encodeURIComponent(addr)}, dataType: 'json',
                success: (data) => {
                    const items = data.items, divAddrConsole = document.getElementById('div-addr-console');

                    items.forEach((i) => {
                        const newDiv = document.createElement('div');
                        newDiv.classList.add('div-restaurant-info');
                        newDiv.classList.add('form-control');
                        newDiv.setAttribute('data-addr', `${i.address}`);
                        newDiv.setAttribute('onclick', `findCoordinate(this)`);
                        const newP = document.createElement('p');
                        const title_ = i.title.replaceAll('<b>', '');
                        const title = title_.replaceAll('</b>', '');
                        const titleNode = document.createTextNode(`업체명: ${title}`);
                        newP.append(titleNode);
                        const newP2 = document.createElement('p');
                        const categoryNode = document.createTextNode(`카테고리: ${i.category}`);
                        newP2.append(categoryNode);
                        const newP3 = document.createElement('p');
                        const addressNode = document.createTextNode(`주소: ${i.address}`);
                        newP3.append(addressNode);
                        newDiv.append(newP);
                        newDiv.append(newP2);
                        newDiv.append(newP3);
                        divAddrConsole.append(newDiv);
                    });
                },
                error: (x, e) => { console.log(x); console.log(e); }
            })
        }
    }
});

function findCoordinate(item) {
    const addr = item.dataset.addr;

    $.ajax({
        type: 'get', url: '/naver/maps', data: {'addr': encodeURIComponent(addr)}, dataType: 'json',
        success: (data) => {
            const addr = data.addresses[0];
            const divAddrConsole = document.getElementById('div-addr-console');
            // divAddrConsole.innerHTML = `<p>도로명 주소: <span id="addr">${addr.roadAddress}</span></p><p>지번 주소: <span id="addr">${addr.jibunAddress}</span></p><p>x: <span id="x">${addr.x}</span></p><p>y: <span id="y">${addr.y}</span></p>`;

            // 마커 표시
            var map = new naver.maps.Map('map', { center: new naver.maps.LatLng(addr.y, addr.x), zoom: 19, zoomControl: true });
            var marker = new naver.maps.Marker({ position: new naver.maps.LatLng(addr.y, addr.x), map: map });

            // 정보창 표시
            var contentString = [`<div class="iw_inner"><p>${addr.jibunAddress}</p><p><a href="https://search.naver.com/search.naver?query=${addr.jibunAddress}" target="_blank">해당 주소 네이버로 검색하기</a></p></div>`].join('');
            var infowindow = new naver.maps.InfoWindow({ content: contentString });
            naver.maps.Event.addListener(marker, "click", (e) => {
                if (infowindow.getMap()) { infowindow.close(); }
                else { infowindow.open(map, marker); }
            });
            infowindow.open(map, marker);
        },
        error: (x, e) => { console.log(x); console.log(e); }
    })
}