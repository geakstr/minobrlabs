using System;
using Xamarin.Forms;
using minobrlabs.Droid;

[assembly: Dependency (typeof(BaseUrl_Android))]
namespace minobrlabs.Droid
{
	public class BaseUrl_Android : IBaseUrl
	{
		public string Get ()
		{
			return "file:///android_asset/";
		}
	}
}

