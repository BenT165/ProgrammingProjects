from django.urls import path
from . import views
#from views import post


urlpatterns = [
    path("", views.index, name="index"),
    path('post/', views.post, name='post'),
]