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
			MainPage = new BasePage ();
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

