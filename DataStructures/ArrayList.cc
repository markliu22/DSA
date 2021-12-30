// ref: https://codereview.stackexchange.com/questions/234556/dynamic-array-implementation-in-c

#include <assert.h>
#include <cstring>
#include <iostream>
using namespace std;

const int INITIAL_CAPACITY = 2;
const int GROWTH_FACTOR = 2;

template <class T>
class dynamic_array {
  T *array;
  int capacity;
  // All identifiers that begin with an underscore and either an uppercase letter or another underscore are always reserved for any use.
  int _size;
public:
  dynamic_array() {
    // TODO: use MIL
    capacity = INITIAL_CAPACITY;
    array = new T[capacity];
    _size = 0;
  }

  ~dynamic_array() {
    delete[] array;
  }

  void deleteAt(int pos) {
    assert(0 <= pos && pos < _size);
    _size--;
    for(int i = pos; i < _size; ++i) {
      array[i] = array[i + 1];
    }
  }

  void insertAt(int element, int pos) {
    assert(0 <= pos && pos <= _size);
    if(_size == capacity) {
      resize();
    }
    for(int i = _size; i > pos; --i) {
      array[i] = array[i - 1];
    }
    _size++;
    array[pos] = element;
  }

  void append(T element) {
    insertAt(element, _size);
  }

  int size() {
    return _size;
  }

  // double internal array capacity if it has to and deletes reference to current array
  void resize() {
    capacity *= GROWTH_FACTOR;
    T *temp = new T[capacity];
    // copy array from 'array' to 'array + _size' to temp
    copy(array, array + _size, temp);
    delete[] array;
    array = temp;
  }

  T get(int pos) {
    return array[pos];
  }

  void pretty_print() {
    cout << "[";
    for(int i = 0; i < _size; ++i) {
      if(i == 0) cout << array[i];
      else cout << " " << array[i];
    }
    cout << "]" << endl;
  }
};

int main() {
  dynamic_array<int> dynArr;
    dynArr.append(3);
    dynArr.append(4);
    dynArr.append(5);
    dynArr.append(4);
    dynArr.append(33);
    dynArr.append(3);
    dynArr.pretty_print();
}
