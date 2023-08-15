import scrapy
from Demo.items import DemoItem 
class Demo(scrapy.Spider):
    name = 'Demo'  
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        # 初始的start_urls
        # self.start_urls = ['http://word.qsbdc.com/wl.php?level=1&page_id=1']
    def start_requests(self):
        # 动态添加start_urls
        for i in range(1, 51):
            
            url = f'http://word.qsbdc.com/wl.php?level=12&page_id={i}'
            yield scrapy.Request(url)

    
    def parse(self,response):
        
        for row in range(3,23):
            rowXpath = "//table[@class='table_solid']/tr["+str(row)+"]"
            demo = DemoItem()
            mes_links = response.xpath(rowXpath)
    
            worldName = mes_links.xpath('td[3]/div/span/text()').extract()[0]
            demo['name'] = worldName
            demo['phonetic'] = mes_links.xpath('td[4]/div/span/text()').extract()[0]
            demo['interpret'] = mes_links.xpath('td[6]/div/span/text()').extract()[0]
            c = worldName[0:1]
            demo['sound'] = 'http://sound.yywz123.com/qsbdcword/'+c+'/'+worldName+'.mp3'
            yield demo
    