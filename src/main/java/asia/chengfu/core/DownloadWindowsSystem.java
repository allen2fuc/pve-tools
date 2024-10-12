package asia.chengfu.core;

import cn.hutool.http.HttpUtil;

public class DownloadWindowsSystem {

    public static void download(String url, String dest) {
        //https://software.download.prss.microsoft.com/dbazure/Win10_22H2_Chinese_Simplified_x64v1.iso?t=571e450d-9d11-4d44-87ab-9b4d4bec387a&P1=1728717091&P2=601&P3=2&P4=YxIavuYv7cNVYdVsYRTALAdhMlcIBlorbkPArLUlPiiSudy0ta7R6jgJUjxOuuEy9ooMvUIkXQzPXcg%2fMpsDWR4Y5YZjxeS7D7fgUjFC03fUdHE0JB%2blgKIjzrXWNx7fn9%2fviYVE0T8Ojis57NiIsFb%2fH4SxIpuXUlfid3O0YntQBpqroOMwDvgaRc0xPL8fi%2fyFAd%2fDXaLrxnxqAVoSYqj9%2fSkYGE9ffTNvoeZDjLXmw%2bMKCsWHxs2AAw9OaR84GLmPhMOeTVoJK8Tu2L%2b8TkGupxHOnOH3c%2fgeE20VuQPpSmoa0tPZ0z2KGrbDBa8LYxy%2bkPwRKoWVrG39j%2bDFqw%3d%3d
        // Windows 10
        HttpUtil.downloadFileFromUrl(url, dest);

        //https://go.microsoft.com/fwlink/p/?LinkID=2195280&clcid=0x804&culture=zh-cn&country=CN
        //Windows Server 2022
    }


}
