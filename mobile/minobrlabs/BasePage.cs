using System;
using System.Threading.Tasks;
using Xamarin.Forms;
using System.Reflection;
using System.IO;

namespace minobrlabs
{
	public class BasePage : ContentPage
	{
		public BasePage ()
		{
			var htmlSource = new HtmlWebViewSource ();
			htmlSource.BaseUrl = DependencyService.Get<IBaseUrl> ().Get ();

			var assembly = typeof(BasePage).GetTypeInfo ().Assembly;
			Stream stream = assembly.GetManifestResourceStream("minobrlabs.index.html");

			using (var reader = new System.IO.StreamReader (stream)) {
				htmlSource.Html = reader.ReadToEnd ();
			}

			var webView = new WebView {
				Source = htmlSource,
				VerticalOptions = LayoutOptions.FillAndExpand
			};

			webView.Navigated += (sender, e) => {
				PassTemperature(webView);
				PassHumidity(webView);
			};

			Content = new StackLayout {
				Padding = new Thickness (0, 20, 0, 0),
				Children = {
					webView
				}
			};
		}

		protected async void PassTemperature (WebView webView)
		{
			for (int i = -50; i <= 60; i += 1) { 
				webView.Eval (String.Format("setTemperature('{0}');", i.ToString()));
				await Task.Delay (100);
			}
		}

		protected async void PassHumidity (WebView webView)
		{
			for (int i = 0; i <= 100; i += 1) { 
				webView.Eval (String.Format("setHumidity('{0}');", i.ToString()));
				await Task.Delay (100);
			}
		}
	}
}

