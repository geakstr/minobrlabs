using System;

using Xamarin.Forms;
using System.Net;
using System.Diagnostics;
using System.Threading.Tasks;

namespace minobrlabs
{
	public class App : Application
	{
		public App ()
		{
			var htmlSource = new HtmlWebViewSource ();
			htmlSource.BaseUrl = DependencyService.Get<IBaseUrl> ().Get ();
			htmlSource.Html =
@"<!DOCTYPE html>
<html>
<head>
	<meta charset=utf-8>
	<title>Xamarin Forms</title>

<script>
function appendHtml(str) {
  var div = document.createElement('div');
  div.innerHTML = str;
  document.body.appendChild(div);
}
</script>
</head>
<body>
	<h1>Xamrin.Forms</h1>
	<p>This is an iOS web page.</p>
</body>
</html>";
	
			var webView = new WebView {
				Source = htmlSource,
				VerticalOptions = LayoutOptions.FillAndExpand
			};

			webView.Navigated += (sender, e) => {
				Print(webView);
			};
				
			MainPage = new ContentPage {
				Content = new StackLayout {
					Padding = new Thickness (0, 20, 0, 0),
					Children = {
						webView
					}
				}
			};
		}

		protected async void Print (WebView webView)
		{
			for (int i = 1; i <= 100; i++) { 
				webView.Eval (String.Format("appendHtml('{0}');", i.ToString()));
				await Task.Delay (1000);
			}
		}

		protected override void OnStart ()
		{
			// Handle when your app starts
		}

		protected override void OnSleep ()
		{
			// Handle when your app sleeps
		}

		protected override void OnResume ()
		{
			// Handle when your app resumes
		}
	}
}

