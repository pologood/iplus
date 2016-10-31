import static lib.BDD.*;

CONFIG(
    server: "http://127.0.0.1",
    headers: [host: "plus.sogou"]
    )

def today = new Date()
def yesterday = today - 1
def todayStr = today.format("yyyy-MM-dd")
def yesterdayStr = yesterday.format("yyyy-MM-dd")

POST("/api/kpi")
EXPECT { 
  json.code = 0 
}

PUT("/api/kpi") {
  r.body = [from: 1, xmId: '70', xmKey: 'j0a37izra1v4n4k0', date: yesterdayStr, 1: '100', 2: '200']
}
EXPECT { 
  json.code = 0 
}

GET("/api/kpi/null") {
  r.query = [date: todayStr]
}
EXPECT { 
  json.code = 0 
  json.data = NotEmpty
}

GET("/api/kpi") {
  r.query = [from: 1, xmId: '70', xmKey: 'j0a37izra1v4n4k0', date: todayStr]
}
EXPECT {
  json.code = 0
  json.data = NotEmpty
}

STAT()