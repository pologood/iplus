import static lib.BDD.*;

PUT("/api/kpi") {
  r.body = [xmId: '70', xmKey: 'j0a37izra1v4n4k0', date: '2015-11-11', 1:'100', 2:'200']
}
EXPECT {
  json.code = 0
}

GET("/api/kpi/null") {
  r.body = [date: '2015-11-11']
}
EXPECT {
  json.code = 0
}