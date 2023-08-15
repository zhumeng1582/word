# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: https://docs.scrapy.org/en/latest/topics/item-pipeline.html


# useful for handling different item types with a single interface
from itemadapter import ItemAdapter
import json#导入Json库


class DemoPipeline:
    def __init__(self):
        self.filename=open("data.json","w")#初始化对象的属性

    def process_item(self, item, spider):
        text = json.dumps(dict(item), ensure_ascii=False)+"\n"
        # bytes to str
        bs = str(text.encode("utf-8"), encoding="utf8")
        self.filename.write(bs)#写入文本
        return item#item必须return

    def close_spider(self, spider):#可选
        self.filename.close()
