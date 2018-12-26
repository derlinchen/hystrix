## SpringCloud ##

**SpringCloud** 是分布式框架，主要处理步骤有：

一、创建服务注册中心，服务注册中心有Eureka、ZooKeeper等。

二、创建微服务服务提供者Provider

三、创建服务调用者Consummer

### SpringCloud版本为Greenwich,以Eureka为例，对SpringCloud简单说明 ###

一、服务注册中心的创建，通过使用`spring-cloud-starter-netflix-eureka-server`进行创建


二、服务提供者创建，通过使用`spring-cloud-starter-netflix-eureka-client`进行创建


三、服务调用者创建，需配置`spring-cloud-starter-netflix-eureka-client`，通过使用`spring-cloud-starter-netflix-ribbon`实现负载均衡

四、断路器hystrix，当服务器发生故障时，通过断路器进行降级处理，以请求未发生故障的服务器。

1、断路器降级配置`@HystrixCommand(fallbackMethod = "methodname")`。

2、通过自定义断路器降级配置，通过基础HystrixCommand类，对类中的`getFallBack()`，`run()`方法进行重写。在使用时，实例化定义的继承类，同时传递`HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("")), RestTemplate`两个参数，通过`execute()`同步调用或`queue()`异步调用。

3、断路器异步使用的其他方式，通过实现`AsyncResult`进行调用，具体如下：

    @HystrixCommand
    public Future<Book> methodname() {
        return new AsyncResult<Book>() {
            @Override
            public Book invoke() {
                return restTemplate.getForObject("http://HELLO-SERVICE/getbook1", Book.class);
            }
        };
    }
4、请求缓存，在继承`HystrixCommand`类中重写`getCacheKey`方法，在请求类中调用如下：

	HystrixCommandKey commandKey = HystrixCommandKey.Factory.asKey("commandKey");
    HystrixRequestContext.initializeContext();
    BookCommand bc1 = new BookCommand(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("")).andCommandKey(commandKey), restTemplate, 1l);
    Book e1 = bc1.execute();

注解开启缓存，使用`@CacheResult`和`@HystrixCommand`对处理请求的service类进行注解，请求类中，执行代码如下：

	/**
	 * service方法
	 */
    @CacheResult
    @HystrixCommand
    public Book test6(Integer id,String aa) {
    	return restTemplate.getForObject("http://HELLO-SERVICE/getbook5/{1}", Book.class, id);
    }

	/**
	 * controller调用service方法
	 */
    HystrixRequestContext.initializeContext();
    //第一次发起请求
    Book b1 = bookService.test6(2, "");

    
通过使用`@CacheKey`来指定缓存的key，代码如下：

    @CacheResult
    @HystrixCommand
    public Book test6(@CacheKey Integer id,String aa) {
    	return restTemplate.getForObject("http://HELLO-SERVICE/getbook5/{1}", Book.class, id);
    }

使用`@CacheRemove`对缓存进行移除，具体实现如下：

    @CacheRemove(commandKey = "test6")
    @HystrixCommand
	public Book test7(@CacheKey Integer id) {
    	return null;
    }

五、声明式服务调用Feign，代替RestTemplate，可以更优雅的进行服务调用

1、消费者中定义调用服务提供者接口，调用具体如下：
    
    @FeignClient("hello-service")
    public interface HelloService {
    	@RequestMapping("/hello")
    	String hello();
    }

表示调用服务提供者hello-service中的hello请求接口

2、在服务提供者hello-service中定义接口：

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
    	return "hello";
    }

3、在Controller层调用消费者代码：

	@RestController
	public class FeignConsumerController {
	    @Autowired
	    HelloService helloService;
	
	    @RequestMapping("/hello")
	    public String hello() {
	        return helloService.hello();
	    }
	}


4、Feign的继承特性

创建公共api，定义接口interface，并在接口中定义调用方法，对接口和方法进行`@RequestMapping`注解

在服务提供者中，实现上述定义的api接口，因为公共api进行了`@RequestMapping`注解，所以提供者中不需要进行`@RequestMapping`注解，在类中必须添加`@RestController`注解

消费者，定义接口继承上述api接口，通过`@FeignClient("hello-service")`进行注解，调用时会调用hello-service提供者的接口，最后定义Controller层，使用`@Autowired`，对定义接口进行注解，最后可直接调用内容提供者的数据。
