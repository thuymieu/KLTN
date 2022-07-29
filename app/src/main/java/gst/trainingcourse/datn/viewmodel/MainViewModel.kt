package gst.trainingcourse.datn.viewmodel

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import gst.trainingcourse.datn.Delegate
import gst.trainingcourse.datn.data.IRepository
import gst.trainingcourse.datn.data.Repository
import gst.trainingcourse.datn.model.*

class MainViewModel : ViewModel() {
    private var repository = Repository()
    private val _listProduct = MutableLiveData<ArrayList<Product>>()
    val listProduct : LiveData<ArrayList<Product>>
    get() = _listProduct

    private val _listProductAll = MutableLiveData<ArrayList<Product>>()
    val listProductAll : LiveData<ArrayList<Product>>
        get() = _listProductAll

    private var _listCategory = MutableLiveData<ArrayList<Category>>()
    val listCategory : LiveData<ArrayList<Category>>
        get() = _listCategory

    private val _listProductWithCategory = MutableLiveData<ArrayList<Product>>()
    val listProductWithCategory : LiveData<ArrayList<Product>>
        get() = _listProductWithCategory

    private val _user = MutableLiveData<ArrayList<User>>()
    val user : LiveData<ArrayList<User>>
        get() = _user

    private val _listFavourite = MutableLiveData<ArrayList<ItemFavourite>>()
    val listFavourite : LiveData<ArrayList<ItemFavourite>>
        get() = _listFavourite

    private val _listOrder = MutableLiveData<ArrayList<Order>>()
    val listOrder : LiveData<ArrayList<Order>>
        get() = _listOrder

    private val _listItemOrder = MutableLiveData<ArrayList<ItemOrder>>()
    val listItemOrder : LiveData<ArrayList<ItemOrder>>
        get() = _listItemOrder

    private val _listNews = MutableLiveData<ArrayList<News>>()
    val listNews : LiveData<ArrayList<News>>
        get() = _listNews

    fun getListProductFromRealtimeDatabase() {
        val myRef = FirebaseDatabase.getInstance().getReference("Products")
        val listProducts = ArrayList<Product>()
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (listProducts != null){
                    listProducts.clear()
                }
                for (dataSnapshot in snapshot.children){
                    val product = dataSnapshot.getValue(Product::class.java)
                    if(product?.status == "Yes"){
                        listProducts.add(product)
                    }
                }
                _listProduct.postValue(listProducts)
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(Delegate.mainActivity,"fail", Toast.LENGTH_LONG).show()
            }
        })
    }
    fun getListProductAllFromRealtimeDatabase() {
        val myRef = FirebaseDatabase.getInstance().getReference("Products")
        val listProducts = ArrayList<Product>()
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (listProducts != null){
                    listProducts.clear()
                }
                for (dataSnapshot in snapshot.children){
                    val product = dataSnapshot.getValue(Product::class.java)
                        listProducts.add(product!!)
                }
                _listProductAll.postValue(listProducts)
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(Delegate.mainActivity,"fail", Toast.LENGTH_LONG).show()
            }
        })
    }
    fun getListProductFromRealtimeDatabaseWithCate(id :Int) {
        val myRef = FirebaseDatabase.getInstance().getReference("Products")
        val listProducts = ArrayList<Product>()
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (listProducts != null){
                    listProducts.clear()
                }
                for (dataSnapshot in snapshot.children){
                    val product = dataSnapshot.getValue(Product::class.java)
                    if(id == product?.id_category && product.status == "Yes"){
                        listProducts.add(product)
                    }
                }
                _listProductWithCategory.postValue(listProducts)
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(Delegate.mainActivity,"fail", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun getListCategoryFromRealtimeDatabase() {
        val myRef = FirebaseDatabase.getInstance().getReference("Brand")
        val listCategory= ArrayList<Category>()
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (listCategory != null){
                    listCategory.clear()
                }
                for (dataSnapshot in snapshot.children){
                    val category = dataSnapshot.getValue(Category::class.java)
                    listCategory.add(category!!)
                }
                _listCategory.postValue(listCategory)
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(Delegate.mainActivity,"fail", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun getUserFromRealtimeDatabase() {
        val myRef = FirebaseDatabase.getInstance().getReference("User")
        val listUser= ArrayList<User>()
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (listUser != null){
                    listUser.clear()
                }
                for (dataSnapshot in snapshot.children){
                    val user = dataSnapshot.getValue(User::class.java)
                    listUser.add(user!!)
                }
                _user.postValue(listUser)
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(Delegate.mainActivity,"fail", Toast.LENGTH_LONG).show()
            }
        })
    }
    fun getFavouriteFromRealtimeDatabase() {
        val myRef = FirebaseDatabase.getInstance().getReference("Wishlist")
        val listProduct= ArrayList<ItemFavourite>()
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (listProduct != null){
                    listProduct.clear()
                }
                for (dataSnapshot in snapshot.children){
                    val item = dataSnapshot.getValue(ItemFavourite::class.java)
                    item?.let { listProduct.add(it) }
                }
                _listFavourite.postValue(listProduct)
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(Delegate.mainActivity,"fail", Toast.LENGTH_LONG).show()
            }
        })
    }
    fun getOrderFromRealtimeDatabase() {
        val myRef = FirebaseDatabase.getInstance().getReference("Order")
        val listOrder = ArrayList<Order>()
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (listOrder != null){
                    listOrder.clear()
                }
                for (dataSnapshot in snapshot.children){
                    val item = dataSnapshot.getValue(Order::class.java)
//                    if (item?.user_id == idUser){
                    item?.let { listOrder.add(it) }
//                    }
                }
                _listOrder.postValue(listOrder)
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(Delegate.mainActivity,"fail", Toast.LENGTH_LONG).show()
            }
        })
    }
    fun getItemOrderFromRealtimeDatabase() {
        val myRef = FirebaseDatabase.getInstance().getReference("ItemOrder")
        val listItemOrder= ArrayList<ItemOrder>()
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (listItemOrder != null){
                    listItemOrder.clear()
                }
                for (dataSnapshot in snapshot.children){
                    val item = dataSnapshot.getValue(ItemOrder::class.java)
                    item?.let { listItemOrder.add(it) }
                }
                _listItemOrder.postValue(listItemOrder)
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(Delegate.mainActivity,"fail", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun getNewsFromRealtimeDatabase() {
        val myRef = FirebaseDatabase.getInstance().getReference("News")
        val listNews= ArrayList<News>()
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (listNews != null){
                    listNews.clear()
                }
                for (dataSnapshot in snapshot.children){
                    val item = dataSnapshot.getValue(News::class.java)
                    item?.let { listNews.add(it) }
                }
                _listNews.postValue(listNews)
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(Delegate.mainActivity,"fail", Toast.LENGTH_LONG).show()
            }
        })
    }
}