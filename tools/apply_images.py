# -*- coding: utf-8 -*-
"""把 UiKit 色块图片占位替换为 Glide 真实图片加载，并把布局 View 改为 ImageView"""
import os
import re

ROOT = os.path.join(os.path.dirname(os.path.dirname(os.path.abspath(__file__))),
                    "app", "src", "main")
JAVA = os.path.join(ROOT, "java", "com", "example", "meituan")
RES = os.path.join(ROOT, "res", "layout")

ADAPTERS = [
    ("customer/RestaurantAdapter.java", "r"),
    ("customer/DishAdapter.java", "d"),
    ("customer/CartAdapter.java", "d"),
    ("customer/SearchResultAdapter.java", "d"),
    ("merchant/DishManageAdapter.java", "d"),
]

for rel, var in ADAPTERS:
    path = os.path.join(JAVA, rel)
    src = open(path, encoding="utf-8").read()
    src = src.replace("import com.example.meituan.util.UiKit;",
                      "import com.example.meituan.util.Images;")
    src = re.sub(r"h\.img\.setBackground\(UiKit\.roundRect\(context, %s\.imageColor, \d+\)\);" % var,
                 "Images.load(h.img, %s.image, %s.imageColor);" % (var, var), src)
    src = src.replace("        View img;", "        ImageView img;")
    if "import android.widget.ImageView;" not in src:
        src = src.replace("import android.widget.TextView;",
                          "import android.widget.ImageView;\nimport android.widget.TextView;")
    open(path, "w", encoding="utf-8", newline="\n").write(src)
    print("java ok:", rel)

for rel, var in [("customer/RestaurantActivity.java", "restaurant"),
                 ("customer/OrderDetailActivity.java", "order.restaurant")]:
    path = os.path.join(JAVA, rel)
    src = open(path, encoding="utf-8").read()
    src = src.replace("import com.example.meituan.util.UiKit;",
                      "import com.example.meituan.util.Images;")
    src = src.replace("View img = findViewById(R.id.img);",
                      "ImageView img = findViewById(R.id.img);")
    src = re.sub(r"img\.setBackground\(UiKit\.roundRect\(this, %s\.imageColor, \d+\)\);" % re.escape(var),
                 "Images.load(img, %s.image, %s.imageColor);" % (var, var), src)
    if "import android.widget.ImageView;" not in src:
        src = src.replace("import android.widget.TextView;",
                          "import android.widget.ImageView;\nimport android.widget.TextView;")
    open(path, "w", encoding="utf-8", newline="\n").write(src)
    print("java ok:", rel)

# 布局：含 @+id/img 的 <View .../> 换成 ImageView 并 centerCrop
for fn in os.listdir(RES):
    if not fn.endswith(".xml"):
        continue
    path = os.path.join(RES, fn)
    src = open(path, encoding="utf-8").read()

    def repl(m):
        tag = m.group(0)
        tag = tag.replace("<View", "<ImageView", 1)
        if "scaleType" not in tag:
            tag = tag.replace('android:id="@+id/img"',
                              'android:id="@+id/img"\n        android:scaleType="centerCrop"', 1)
        return tag

    new = re.sub(r"<View\b[^>]*@\+id/img[^>]*/>", repl, src, flags=re.S)
    if new != src:
        open(path, "w", encoding="utf-8", newline="\n").write(new)
        print("layout ok:", fn)
print("ALL DONE")
