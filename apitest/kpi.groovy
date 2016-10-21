import static lib.BDD.*;

CONFIG(
  server: "http://127.0.0.1",
  headers: [host: "plus.sogou"]
)


PUT("/api/kpi") {
  r.body = [xmId: '70', xmKey: 'j0a37izra1v4n4k0', date: '2015-11-10', 1:'100', 2:'200']
}
EXPECT {
  json.code = 0
}

GET("/api/kpi/null") {
  r.query = [date: '2015-11-10']
}
EXPECT {
  json.code = 0
}

GET("/api/kpi") {
  r.query = [beginDate: '2015-11-10', endDate:'2015-11-12']
}
EXPECT {
  json.code = 0
  json.data = NotEmpty
}

STAT()