# Define here the models for your scraped items
#
# See documentation in:
# https://docs.scrapy.org/en/latest/topics/items.html

import scrapy
class DemoItem(scrapy.Item):
    # define the fields for your item here like:
    name = scrapy.Field()
    phonetic = scrapy.Field()
    interpret = scrapy.Field()
    example = scrapy.Field()
    sound = scrapy.Field()
    pass
