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
				PassData(webView);
			};

			Content = new StackLayout {
				Padding = new Thickness (0, 20, 0, 0),
				Children = {
					webView
				}
			};
		}

		protected async void PassData (WebView webView)
		{
			for (int i = 0; i <= 3000; i += 100) { 
				webView.Eval (String.Format("setTemperature('{0}');", i.ToString()));
				await Task.Delay (1000);
			}
		}
	}
}

