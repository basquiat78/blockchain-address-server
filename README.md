# blockchain-address-service

이 프로젝트는 redis의 pub/sub을 이용한 프로젝트이다.

pub/sub을 지원하는 많은 application, 예를 들면 kafka, rabbitmq같은 것도 있을 수 있겠지만 가벼우면서도 빠르게 사용할 수 있는 redis를 선택했고 queue, stack 메세지를 구현하는것도 상당히 쉽다.

MSA를 구성할 때 각 서비스들을 Eureka와 Feign Client를 활용해 각 각의 서비스들을 호출할 수 있게 구성할 수 있다.    

하지만 필요해 의해 해당 서비스의 존재를 알지 못하더라도 redis의 pub/sub을 통해서 다른 서비스를 호출 하는 것이 가능하고 그것을 subscribe를 통해서 리턴을 받을 수 있다.

Spring Boot를 이용한 서비스이지만 Controller가 존재하지 않고 오직 특정 채널을 Subscribe하고 다른 API Server가 해당 특정 채널로 Publish할 때 이벤트를 받아서 서비스를 처리하는 방식으로 구현되어 있다.

[위키 참조](https://github.com/basquiat78/blockchain-address-server/wiki)
