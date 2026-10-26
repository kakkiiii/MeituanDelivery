# -*- coding: utf-8 -*-
"""下载真实食物照片到 app/src/main/assets/food/（TheMealDB / TheCocktailDB 免费图库）"""
import json
import os
import time
import urllib.request

PROXY = "http://127.0.0.1:7897"
BASE = os.path.join(os.path.dirname(os.path.dirname(os.path.abspath(__file__))),
                    "app", "src", "main", "assets", "food")
os.makedirs(BASE, exist_ok=True)
opener = urllib.request.build_opener(
    urllib.request.ProxyHandler({"http": PROXY, "https": PROXY}))
opener.addheaders = [("User-Agent", "Mozilla/5.0")]

MEAL = "https://www.themealdb.com/api/json/v1/1/"
DRINK = "https://www.thecocktaildb.com/api/json/v1/1/"


def get(url):
    for attempt in range(3):
        try:
            with opener.open(url, timeout=30) as r:
                return r.read()
        except Exception as e:
            print("retry", attempt, url, e, flush=True)
            time.sleep(1)
    return None


def meals_of(url):
    raw = get(url)
    if not raw:
        return []
    try:
        data = json.loads(raw)
        return (data.get("meals") or data.get("drinks") or [])
    except Exception:
        return []


# (保存前缀, 来源URL列表, 需要张数)
PLAN = [
    ("chicken", [MEAL + "filter.php?c=Chicken"], 8),
    ("beef", [MEAL + "filter.php?c=Beef"], 6),
    ("pork", [MEAL + "filter.php?c=Pork"], 6),
    ("fish", [MEAL + "filter.php?c=Seafood"], 6),
    ("noodle", [MEAL + "filter.php?c=Pasta"], 6),
    ("rice", [MEAL + "search.php?s=rice"], 5),
    ("dessert", [MEAL + "filter.php?c=Dessert"], 6),
    ("salad", [MEAL + "filter.php?c=Vegetarian", MEAL + "filter.php?c=Vegan"], 5),
    ("pizza", [MEAL + "search.php?s=pizza"], 3),
    ("soup", [MEAL + "search.php?s=soup"], 4),
    ("breakfast", [MEAL + "filter.php?c=Breakfast"], 3),
    ("drink", [DRINK + "filter.php?c=Shake", DRINK + "filter.php?c=Ordinary_Drink",
               DRINK + "filter.php?c=Other / Unknown"], 8),
]

manifest = {}
for prefix, urls, want in PLAN:
    pool = []
    for u in urls:
        pool.extend(meals_of(u))
    seen = set()
    imgs = []
    for m in pool:
        thumb = m.get("strMealThumb") or m.get("strDrinkThumb")
        name = m.get("strMeal") or m.get("strDrink") or "?"
        if not thumb or thumb in seen:
            continue
        seen.add(thumb)
        imgs.append((thumb, name))
    saved = []
    for i, (thumb, name) in enumerate(imgs[:want], 1):
        fn = "%s_%d.jpg" % (prefix, i)
        path = os.path.join(BASE, fn)
        if os.path.exists(path) and os.path.getsize(path) > 1000:
            saved.append(fn)
            continue
        # preview 小图，体积小适合打包
        data = get(thumb + "/preview")
        if not data or len(data) < 1000:
            data = get(thumb)
        if data and len(data) > 1000:
            with open(path, "wb") as f:
                f.write(data)
            saved.append(fn)
            print("OK", fn, len(data), name, flush=True)
        else:
            print("FAIL", fn, name, flush=True)
    manifest[prefix] = saved

with open(os.path.join(BASE, "manifest.json"), "w", encoding="utf-8") as f:
    json.dump(manifest, f, ensure_ascii=False, indent=1)
print("DONE", {k: len(v) for k, v in manifest.items()}, flush=True)
