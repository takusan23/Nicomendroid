package io.github.takusan23.nicomendroid.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.github.takusan23.nicomendroid.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class LicenseFragment extends Fragment {
    private View view;
    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_license, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textView = view.findViewById(R.id.license_textView);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.license));

        String okhttp = "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                "you may not use this file except in compliance with the License.\n" +
                "You may obtain a copy of the License at\n" +
                "\n" +
                "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                "\n" +
                "Unless required by applicable law or agreed to in writing, software\n" +
                "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                "See the License for the specific language governing permissions and\n" +
                "limitations under the License.";

        String glide = "Copyright 2014 Google, Inc. All rights reserved.\n" +
                "\n" +
                "Redistribution and use in source and binary forms, with or without modification, are\n" +
                "permitted provided that the following conditions are met:\n" +
                "\n" +
                "   1. Redistributions of source code must retain the above copyright notice, this list of\n" +
                "         conditions and the following disclaimer.\n" +
                "\n" +
                "   2. Redistributions in binary form must reproduce the above copyright notice, this list\n" +
                "         of conditions and the following disclaimer in the documentation and/or other materials\n" +
                "         provided with the distribution.\n" +
                "\n" +
                "THIS SOFTWARE IS PROVIDED BY GOOGLE, INC. ``AS IS'' AND ANY EXPRESS OR IMPLIED\n" +
                "WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND\n" +
                "FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GOOGLE, INC. OR\n" +
                "CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR\n" +
                "CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR\n" +
                "SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON\n" +
                "ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING\n" +
                "NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF\n" +
                "ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\n" +
                "\n" +
                "The views and conclusions contained in the software and documentation are those of the\n" +
                "authors and should not be interpreted as representing official policies, either expressed\n" +
                "or implied, of Google, Inc.";

        String websocket = " Copyright (c) 2010-2019 Nathan Rajlich\n" +
                "\n" +
                " Permission is hereby granted, free of charge, to any person\n" +
                " obtaining a copy of this software and associated documentation\n" +
                " files (the \"Software\"), to deal in the Software without\n" +
                " restriction, including without limitation the rights to use,\n" +
                " copy, modify, merge, publish, distribute, sublicense, and/or sell\n" +
                " copies of the Software, and to permit persons to whom the\n" +
                " Software is furnished to do so, subject to the following\n" +
                " conditions:\n" +
                "\n" +
                " The above copyright notice and this permission notice shall be\n" +
                " included in all copies or substantial portions of the Software.\n" +
                "\n" +
                " THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND,\n" +
                " EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES\n" +
                " OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND\n" +
                " NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT\n" +
                " HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,\n" +
                " WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING\n" +
                " FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR\n" +
                " OTHER DEALINGS IN THE SOFTWARE.";

        String okhttp_title = "square/okhttp https://github.com/square/okhttp";
        String glide_title = "bumptech/glide https://github.com/bumptech/glide";
        String websocket_title = "TooTallNate/Java-WebSocket https://github.com/TooTallNate/Java-WebSocket";

        textView.setText(okhttp_title);
        textView.append("\n");
        textView.append(okhttp);
        textView.append("\n");
        textView.append("-----------");
        textView.append("\n");
        textView.append(glide_title);
        textView.append("\n");
        textView.append(glide);
        textView.append("\n");
        textView.append("-----------");
        textView.append("\n");
        textView.append(websocket_title);
        textView.append("\n");
        textView.append(websocket);
        textView.append("\n");
        textView.append("-----------");


    }
}
