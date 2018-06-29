package Database;

import Data.Suggestion;
import Models.CodeItem;
import Models.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CodeManager {

    /*
        HashSet for storing CodeItems
        This way only first file creates CodeItem object.
     */
    private HashMap <String, CodeItem> codeItems;
    private HashMap <String, CodeFile> codeFiles;
    private User user;

    public CodeManager (User user) {
        //Temporary
        ArrayList<Suggestion> suggestions = new ArrayList<>();
        String suggestionContent1 = "<pre><code>    scanf (&quot;%d %d\\n&quot;, &amp;n, &amp;m);\n" +
                "    plane.resize (n);\n" +
                "    for (int i = 0; i &lt; n; i ++) {\n" +
                "        scanf (&quot;%s\\n&quot;, line);\n" +
                "        plane[i] = string(line);\n" +
                "        for (int j = 0; j &lt; m; j ++) {\n" +
                "            if (plane[i][j] == &#39;1&#39;) {\n" +
                "                s = {i, j};\n" +
                "                plane[i][j] = &#39;.&#39;;\n" +
                "            }\n" +
                "            if (plane[i][j] == &#39;2&#39;) {\n" +
                "                f = {i, j};\n" +
                "                plane[i][j] = &#39;.&#39;;\n" +
                "            }\n" +
                "        }\n" +
                "    }</code></pre><p>კარგია!!! (<em>დახრილი</em>, <strong>მუქი</strong>)</p>";

        String code = "#include <bits/stdc++.h>\n" +
                "\n" +
                "#define mod 1000000009\n" +
                "#define x first\n" +
                "#define y second\n" +
                "using namespace std;\n" +
                "\n" +
                "    int n, m;\n" +
                "    vector <string> plane;\n" +
                "    char line[200005];\n" +
                "    pair <int, int> s, f;\n" +
                "    pair <int, int> h[4] = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};\n" +
                "    int max_count = 0;\n" +
                "    int min_count = INT_MAX;\n" +
                "\n" +
                "    bool is_valid (pair <int, int> a) {\n" +
                "        if (a.x < 0 || a.y < 0 || a.x > n-1 || a.y > m-1) {\n" +
                "            return false;\n" +
                "        }\n" +
                "        else return plane[a.x][a.y] == '.';\n" +
                "    }\n" +
                "\n" +
                "    int get_adj_cnt (pair <int, int> x) {\n" +
                "        int cnt = 0;\n" +
                "        for (int i = 0; i < 4; i ++) {\n" +
                "            pair <int, int> to = {x.x+h[i].x, x.y+h[i].y};\n" +
                "            cnt += is_valid (to);\n" +
                "        }\n" +
                "\n" +
                "        return cnt;\n" +
                "    }\n" +
                "\n" +
                "    bool bfs () {\n" +
                "        queue <pair <int, int> > q;\n" +
                "        q.push (s);\n" +
                "        while (q.size()) {\n" +
                "            pair <int, int> cur = q.front();\n" +
                "            q.pop();\n" +
                "            if (cur.x == f.x && cur.y == f.y) {\n" +
                "                return true;\n" +
                "            }\n" +
                "\n" +
                "            for (int i = 0; i < 4; i ++) {\n" +
                "                pair <int, int> to = {cur.x+h[i].x, cur.y+h[i].y};\n" +
                "                if (is_valid(to)) {\n" +
                "                    plane[to.x][to.y] = '#';\n" +
                "                    q.push (to);\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "        return false;\n" +
                "    }   \n" +
                "\n" +
                "int main () {\n" +
                "    \n" +
                "    //freopen (\"input.txt\", \"r\", stdin);\n" +
                "    \n" +
                "    scanf (\"%d %d\\n\", &n, &m);\n" +
                "    plane.resize (n);\n" +
                "    for (int i = 0; i < n; i ++) {\n" +
                "        scanf (\"%s\\n\", line);\n" +
                "        plane[i] = string(line);\n" +
                "        for (int j = 0; j < m; j ++) {\n" +
                "            if (plane[i][j] == '1') {\n" +
                "                s = {i, j};\n" +
                "                plane[i][j] = '.';\n" +
                "            }\n" +
                "            if (plane[i][j] == '2') {\n" +
                "                f = {i, j};\n" +
                "                plane[i][j] = '.';\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    for (int i = 0; i < n; i ++) {\n" +
                "        for (int j = 0; j < m; j ++) {\n" +
                "            if (plane[i][j] == '.') {\n" +
                "                max_count = max(max_count, get_adj_cnt({i, j}));\n" +
                "                min_count = min(min_count, get_adj_cnt({i, j}));\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "    if (max_count > 2 || min_count == 2 && bfs()) {\n" +
                "        printf (\"YES\");        \n" +
                "    } else {\n" +
                "        printf (\"NO\");\n" +
                "    }\n" +
                "\n" +
                "    return 0;\n" +
                "}";


        Suggestion simpleSuggestion1 = new Suggestion(Suggestion.SuggestionType.Error, "Giorgi Baghdavadze", "https://api.adorable.io/avatars/285/giorgi-bagdavadze.png",  "1", "1", 74, 90, suggestionContent1, new Date());
        Suggestion simpleSuggestion2 = new Suggestion(Suggestion.SuggestionType.Warning, "Bakur Tsutskhashvili", "https://api.adorable.io/avatars/285/bakuri-tsutskhashvili.png", "1", "2",  3, 4, "Not so bad", new Date());
        suggestions.add(simpleSuggestion1);
        suggestions.add(simpleSuggestion2);
        CodeFile codeFile = new CodeFile(code, "sd","code_file1.cpp", suggestions);

        this.user = user;
        this.codeItems = new HashMap<>();
        this.codeFiles = new HashMap<>();
        this.codeFiles.put("code_file1.cpp", codeFile);
    }

    /**
     * Gets requested file.
     * @param name name of file
     * @return Text corresponding for this file
     * @throws NullPointerException in case there is no data for this file
     */
    public String get(String name) throws NullPointerException {
        if (codeFiles.containsKey(name)) {
            return codeFiles.get(name).toString();
        }
        throw new NullPointerException();
    }
}
