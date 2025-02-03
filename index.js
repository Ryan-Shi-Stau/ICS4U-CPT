const fs = require('node:fs');

const ranksThresoulds = {
  "top1": 0.000000,
  "x+": 0.002,
  "x": 0.01,
  "u": 0.05,
  "ss": 0.11,
  "s+": 0.17,
  "s": 0.23,
  "s-": 0.3,
  "a+": 0.38,
  "a": 0.46,
  "a-": 0.54,
  "b+": 0.62,
  "b": 0.7,
  "b-": 0.78,
  "c+": 0.84,
  "c": 0.9,
  "c-": 0.95,
  "d+": 0.975,
  "d": 1,
  //"z": -1,
  //"": 0.5
}

function wait(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

async function doShit(after) {
  let link = "https://ch.tetr.io/api/users/by/league?limit=100";
  if (after !== undefined) {
    link += `&after=${after}:0:0`
  }
  let lb = await fetch(link,
    {
      headers: {
        "X-Session-ID": "Creamy Kagarin",
        "User-Agent": "beanserver blaster but with usernames lmao"
      }
    }
  )
  if (lb.status !== 200) {
    throw Error(`Got ${lb.status}: ${lb.statusText}`)
  }
  const json = await lb.json();
  if (json.success) {
    return json.data.entries;
  } else {
    throw Error(`They are not okay with that: ${json.error}`)
  }

}

var after = 25000
var responseLength = 100;
var leaderboard = [];
doShit().then(async (response) => {
  leaderboard.push(...response);
  after = leaderboard[leaderboard.length - 1].league.tr;
  while (responseLength > 0) {
    await wait(1750);
    let requestedBit = await doShit(after)
    if (requestedBit.length === 0) break;
    leaderboard.push(...requestedBit);
    after = requestedBit[requestedBit.length - 1].league.tr ?? 0;
    responseLength = requestedBit.length;
    console.log(`${leaderboard.length}, ${after}`);
  }

  var thresoulds = {}

  for (const key in ranksThresoulds) {
    let pos = key == "top1" ? 0 : Math.trunc(leaderboard.length * ranksThresoulds[key]) - 1;
    let glicko = leaderboard[pos]["league"]["glicko"];
    while (leaderboard[pos]["league"]["rd"] > 65 && key !== "top1") {
      pos--;
      glicko = leaderboard[pos]["league"]["glicko"];
    }
    //console.log(`${key}: ${leaderboard[pos]["league"]["glicko"]} with ${leaderboard[pos]["league"]["rd"]} RD (#${pos})`);
    thresoulds[key] = {
      tr: leaderboard[key == "top1" ? 0 : Math.trunc(leaderboard.length * ranksThresoulds[key]) - 1]["league"]["tr"],
      glicko: leaderboard[pos]["league"]["glicko"],
      gxe: leaderboard[pos]["league"]["gxe"]
    }
  }
  //console.log(thresoulds);
  var exportTresoulds = {
    "created": Date.now(),
    "cache_until": Date.now() + 3600000,
    "data": thresoulds
  }
  var exportLeaderboard = {
    "created": Date.now(),
    "cache_until": Date.now() + 3600000,
    "data": leaderboard
  }

  fs.writeFile('leaderboard.json', JSON.stringify(exportLeaderboard), err => {
    if (err) {
      console.error(err);
    } else {
      console.log("leaderboard.json was updated");
    }
  }
  );

  var leaderboardCSV = "username,tr,rank,glicko,rd,apm,pps,vs";

  for (const key in leaderboard) {
    leaderboardCSV += `\n${leaderboard[key]['username']},${leaderboard[key]['league']['tr']},${leaderboard[key]['league']['rank']},${leaderboard[key]['league']['glicko']},${leaderboard[key]['league']['rd']},${leaderboard[key]['league']['apm']},${leaderboard[key]['league']['pps']},${leaderboard[key]['league']['vs']}`
  }

  fs.writeFile('leaderboard.csv', leaderboardCSV, err => {
    if (err) {
      console.error(err);
    } else {
      console.log("leaderboard.json was updated");
    }
  }
  );

  var historyExport = `\n${Math.trunc(Date.now() / 1000)}`;


});
